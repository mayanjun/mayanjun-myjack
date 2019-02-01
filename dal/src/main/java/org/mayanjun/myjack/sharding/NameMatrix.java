/*
 * Copyright 2016-2018 mayanjun.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mayanjun.myjack.sharding;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * NameMatrix：描述了实体和数据规模的对应关系
 *
 * @author mayanjun(23/09/2016)
 */
public class NameMatrix implements Serializable {

	private static final ObjectMapper OBJECT_MAPPER;
	private static final TypeReference<Map<String, Set<String>>> MATRIX_TPYE_REFERENCE = new TypeReference<Map<String, Set<String>>>() {};

	static {
		OBJECT_MAPPER = new ObjectMapper();
	}

	/**
	 * KEY: 实体名称
	 * VALUE: Scale 对应的数据库名称和表名称
	 */
	private Map<String, TreeMap<Long, DTNames>> matrix;

	/**
	 * 一个实体映射的所有数据库名称和表名称，方便用来做全库全表扫描
	 * KEY: 实体名称
	 * VALUE: 数据库名称:[表名称]
	 */
	private Map<String, Map<String, Set<String>>> mergedNameMap;

	/**
	 * mergedNameMap value 的序列化形式，为了在返回时复制
	 */
	private Map<String, String> mergedNameMapJson;

	/**
	 * 存储当前调用的表名
	 */
	private final ThreadLocal<String> tableName;

	public NameMatrix(List<ScaleOutMetadata> metadatas) {
		this.tableName = new ThreadLocal<String>();
		this.mergedNameMap = new TreeMap<String, Map<String, Set<String>>>();
		this.mergedNameMapJson = new HashMap<String, String>();
		initMatrix(metadatas);
	}

	private void initMatrix(List<ScaleOutMetadata> metadatas) {
		if (metadatas == null || metadatas.isEmpty()) {
			throw new IllegalArgumentException("metadatas may not be empty");
		}
		this.matrix = new HashMap<String, TreeMap<Long, DTNames>>();

		for (ScaleOutMetadata metadata : metadatas) {
			String entityName = metadata.getEntityName();
			try {
				Long scale = metadata.getScale();
				if (scale == null) throw new IllegalArgumentException("scale may not be null");
				Map<String, Set<String>> dtMap = OBJECT_MAPPER.readValue(metadata.getMatrixJson(), MATRIX_TPYE_REFERENCE);
				DTNames names = this.new DTNames(scale, dtMap);

				TreeMap<Long, DTNames> scaleTable = matrix.get(entityName);
				if(scaleTable == null) {
					scaleTable = new TreeMap<Long, DTNames>();
					matrix.put(entityName, scaleTable);
				}
				if(scaleTable.containsKey(scale)) {
					String message = String.format("Duplicated scale %d in entity %s", scale, entityName);
					throw new IllegalArgumentException(message);
				}
				scaleTable.put(scale, names);
				mergeNameMap(entityName, names);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取指定实体名称下的所有数据库名称和表名称
	 * @param entityName
	 * @return
	 */
	public Map<String, Set<String>> getDataBaseNames(String entityName) {
		String json = mergedNameMapJson.get(entityName);
		if(json != null) {
			try {
				return OBJECT_MAPPER.readValue(json, MATRIX_TPYE_REFERENCE);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		Map<String, Set<String>> map = mergedNameMap.get(entityName);
		if(map != null) {
			try {
				String json1 = OBJECT_MAPPER.writeValueAsString(map);
				mergedNameMapJson.put(entityName, json1);
				return getDataBaseNames(entityName);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 合并同一个实体在数据库中被存储的数据库名称和表名称
	 * @param entityName
	 * @param names
	 */
	private void mergeNameMap(String entityName, DTNames names) {
		Map<String, Set<String>> map = this.mergedNameMap.get(entityName);
		if(map == null) {
			map = new HashMap<String, Set<String>>();
			mergedNameMap.put(entityName, map);
		}
		Iterator<Map.Entry<String, String[]>> iterator = names.nameMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, String[]> entry = iterator.next();
			String dbName = entry.getKey();
			Set<String> tableNames = map.get(dbName);
			if(tableNames == null) {
				tableNames = new HashSet<String>();
				map.put(dbName, tableNames);
			}
			for(String s : entry.getValue()) {
				tableNames.add(s);
			}
		}
	}

	private String mergeredMapToString() {
		StringBuilder sb = new StringBuilder();
		Iterator<Map.Entry<String, Map<String, Set<String>>>> it = mergedNameMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Map<String, Set<String>>> e = it.next();
			sb.append(e.getKey() + ":\r\n");
			Iterator<Map.Entry<String, Set<String>>> it2 = e.getValue().entrySet().iterator();
			while (it2.hasNext()) {
				Map.Entry<String, Set<String>> e2 = it2.next();
				sb.append("\t" + e2.getKey() + ": ").append(e2.getValue().toString() + "\r\n");
			}
			sb.append("-----------------\r\n");
		}
		return sb.toString();
	}

	public static String serialize(Map<String, Set<String>> matrix) {
		Assert.isTrue(matrix != null && !matrix.isEmpty(), "Matrix map can not be empty");
		try {
			Map<String, Set<String>> matrix0 = new TreeMap<String, Set<String>>();
			for(Map.Entry<String, Set<String>> entry : matrix.entrySet()) {
				Set<String> set = entry.getValue();
				if(set != null) {
					set = new TreeSet<String>(set);
					matrix0.put(entry.getKey(), set);
				}
			}
			return OBJECT_MAPPER.writeValueAsString(matrix0);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析字符串并序列化分库分表源数据。数据格式：dbName:t1,t2,...,tN;db2=t1,t2,...,tN;...
	 * @param matrix
	 * @return
	 */
	public static String serialize(String matrix) {
		Assert.isTrue(StringUtils.isNotBlank(matrix), "Matrix string can not be empty");

		String s0[] = matrix.split(";");
		Map<String, Set<String>> matrixMap = new HashMap<String, Set<String>>(s0.length);
		for(String s : s0) {
			String s1[] = s.split(":");
			if(s1.length == 2) {
				String key = StringUtils.deleteWhitespace(s1[0]);
				String value = s1[1];
				String ts[] = value.split(",");
				if(ts.length > 0) {
					Set<String> set = new HashSet<String>();
					for(String t : ts) set.add(StringUtils.deleteWhitespace(t));
					matrixMap.put(key, set);
				}
			}
		}
		return serialize(matrixMap);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if(!this.matrix.isEmpty()) {
			Iterator<Map.Entry<String, TreeMap<Long, DTNames>>> it = matrix.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, TreeMap<Long, DTNames>> entry = it.next();
				sb.append(entry.getKey() + ":\r\n");
				TreeMap<Long, DTNames> map = entry.getValue();
				Iterator<Map.Entry<Long, DTNames>> it1 = map.entrySet().iterator();
				while (it1.hasNext()) {
					Map.Entry<Long, DTNames> entry1 = it1.next();
					sb.append("\t" + entry1.getKey() + " > " + entry1.getValue().toString() + "\r\n");
				}
				//sb.append("----------------------\r\n");
			}
			sb.append("================ Merged Name Map ===============\r\n");
			sb.append(mergeredMapToString());
			return sb.toString();
		} else {
			return "null";
		}
	}

	/**
	 * 此处TableName顺便也算出来
	 * @param scale
	 * @param entityName
	 * @return
	 */
	public String getDataBaseName(long scale, String entityName) {
		TreeMap<Long, DTNames> map = matrix.get(entityName);
		if(map != null) {
			Map.Entry<Long, DTNames> entry = map.floorEntry(scale);
			DTNames names = entry.getValue();
			Long index = scale % names.dbNames.length;

			String dbName = names.dbNames[index.intValue()];
			// 计算TableName
			String[] tableNames = names.nameMap.get(dbName);
			Long tableIndex = scale % tableNames.length;
			this.tableName.set(tableNames[tableIndex.intValue()]);

			return dbName;
		}
		throw new IllegalArgumentException("scale map not found: scale=" + scale + ", entityName=" + entityName);
	}

	public String getTableName() {
		return tableName.get();
	}

	////////////////////////////////////////////////////////////////

	/**
	 * 描述了数据库名称和表名称的对应关系
	 */
	class DTNames {
		private long scale;
		private String dbNames[];
		private Map<String, String[]> nameMap;

		public DTNames(long scale, Map<String, Set<String>> nameMap) {
			if(scale < 0) throw new IllegalArgumentException("scale can not be a negative integer");
			this.scale = scale;
			init(nameMap);
		}

		private void init(Map<String, Set<String>> nameMap) {
			if(nameMap == null || nameMap.isEmpty()) throw new IllegalArgumentException("NameMap can not be empty:");
			Map<String, String[]> map = new TreeMap<String, String[]>();
			Iterator<Map.Entry<String, Set<String>>> iterator = nameMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Set<String>> entry = iterator.next();
				String name = entry.getKey();
				Set<String> set = new TreeSet<String>(entry.getValue());
				String tableNames[] = new String[set.size()];
				set.toArray(tableNames);
				map.put(name, tableNames);
			}
			this.nameMap = map;
			Set<String> dbNamesSet = this.nameMap.keySet();
			this.dbNames = new String[dbNamesSet.size()];
			dbNamesSet.toArray(dbNames);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("scale=" + scale + ",dbNames=[");
			for(int i = 0; i < dbNames.length; i++) {
				sb.append(dbNames[i]);
				if(i < dbNames.length - 1) sb.append(",");
			}
			sb.append("], matrix={");
			Iterator<Map.Entry<String, String[]>> it = nameMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String[]> entry = it.next();
				sb.append(entry.getKey()).append("=").append(ArrayUtils.toString(entry.getValue())).append(",");
			}
			int lastIndex = sb.length() - 1;
			if(sb.charAt(lastIndex) == ',') sb.setCharAt(lastIndex, '}');
			return sb.toString();
		}
	}
}