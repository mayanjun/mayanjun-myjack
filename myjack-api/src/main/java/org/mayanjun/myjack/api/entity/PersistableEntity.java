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

package org.mayanjun.myjack.api.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * This class represents a persistable entity.
 * @author mayanjun
 * @since 0.0.1
 */
public interface PersistableEntity<P extends Serializable> extends Serializable {

	/**
	 * Returns the ID of the persistent entity
	 *
	 * @return ID
	 */
	P getId();
	void setId(P id);

	/**
	 * Returns the created time
	 *
	 * @return the date that the entity created
	 */
	Date getCreatedTime();

	/**
	 * Set created time
	 * @param createdTime time
	 */
	void setCreatedTime(Date createdTime);

	/**
	 * Returns the modified time
	 *
	 * @return the date of the entity modified
	 */
	Date getModifiedTime();

	/**
	 * Set modified time
	 * @param modifiedTime time
	 */
	void setModifiedTime(Date modifiedTime);
}
