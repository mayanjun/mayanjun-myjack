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


import org.mayanjun.myjack.api.annotation.Column;
import org.mayanjun.myjack.api.enums.DataType;

/**
 * Editable entity. Tow edit related field can be inherited
 */
public abstract class EditableEntity extends EntityBean {

	public EditableEntity() {
    }

	public EditableEntity(Long id) {
		super(id);
	}

	/**
	 *
	 */
	@Column(type = DataType.VARCHAR, length = "32", comment = "Creator")
	private String creator;

	@Column(type = DataType.VARCHAR, length = "32", comment = "Last editor")
	private String editor;

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}
}