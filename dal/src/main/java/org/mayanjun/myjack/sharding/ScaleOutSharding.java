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

import org.mayanjun.myjack.Sharding;
import org.mayanjun.myjack.core.entity.EntityBean;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ScaleOutSharding
 *
 * @author mayanjun(23/09/2016)
 */
public class ScaleOutSharding implements Sharding {

	private NameMatrix matrix;

	public ScaleOutSharding() {
	}

	public ScaleOutSharding(List<ScaleOutMetadata> metadatas) {
		setMetadatas(metadatas);
	}

	@Override
	public String getDatabaseName(Object source) {
		if(source instanceof EntityBean) {
			Long id = ((EntityBean) source).getId();
			return this.matrix.getDataBaseName(id, source.getClass().getCanonicalName());
		}
		return null;
	}

	@Override
	public String getTableName(Object source) {
		return matrix.getTableName();
	}

	@Override
	public Map<String, Set<String>> getDatabaseNames(Object source) {
		return matrix.getDataBaseNames(source.getClass().getCanonicalName());
	}

	public void setMetadatas(List<ScaleOutMetadata> metadatas) {
		this.matrix = new NameMatrix(metadatas);
	}
}
