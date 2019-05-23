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

package org.mayanjun.myjack.generator;

import org.mayanjun.myjack.api.annotation.Table;
import org.mayanjun.myjack.api.entity.PersistableEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DDLGenerator
 *
 * @author mayanjun(14/11/2016)
 *
 * @since 0.0.4
 */
public class DDL {

	private static final Logger LOG = LoggerFactory.getLogger(DDL.class);
	private static MysqlSchemeGenerator GENERATOR = new MysqlSchemeGenerator(true);
	private static TypeFilter FILTER = new AnnotationTypeFilter(Table.class);
	private static ResourcePatternResolver RESOLVER = new PathMatchingResourcePatternResolver();
	private static MetadataReaderFactory READER_FACTORY = new CachingMetadataReaderFactory(RESOLVER);


	public static List<Resource> parseResources(String... packageName) throws Exception {
		List<Resource> list = new ArrayList<Resource>();
		for(String pkg : packageName) {
			String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(pkg) + "/**/*.class";
			Resource resources[] = RESOLVER.getResources(pattern);
			list.addAll(Arrays.asList(resources));
		}
		return list;
	}


	public static String ddl(Class<? extends PersistableEntity> clazz, String tableName, boolean genDrop) throws Exception {
		return GENERATOR.generate(clazz, tableName, genDrop);
	}

	public static void ddl(File outFile, boolean includeJar, String... packageName) throws Exception {
		ddl(outFile, includeJar, true, packageName);
	}

	public static List<String> ddl(boolean includeJar, boolean genDrop, String... packageName) {
		List<String> list = new ArrayList<String>();
		try {
			List<Resource> resources = parseResources(packageName);
			for (Resource r : resources) {
				if(!includeJar) {
					URI uri = r.getURI();
					String scheme = uri.getScheme();
					if("jar".equals(scheme)) continue;
				}
				MetadataReader reader = READER_FACTORY.getMetadataReader(r);
				boolean matched = FILTER.match(reader, READER_FACTORY);
				if (matched) {
					String className = reader.getClassMetadata().getClassName();
					Class<?> cls = Class.forName(className);
					LOG.info("Generating: " + className);
					list.add(GENERATOR.generate(cls, null, genDrop));
				}
			}
		} catch (Exception e) {
			LOG.error("Generate ddl error", e);
		}
		return list;
	}

	public static void ddl(File outFile, boolean includeJar, boolean genDrop, String... packageName) throws Exception {
		FileWriter writer = null;
		try {
			writer = new FileWriter(outFile);
			List<String> list = ddl(includeJar, genDrop, packageName);
			for (String s : list) writer.write(s);
			writer.flush();
			LOG.info("DDL write success!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
