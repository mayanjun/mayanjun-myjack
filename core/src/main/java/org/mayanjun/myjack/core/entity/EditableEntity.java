package org.mayanjun.myjack.core.entity;


import org.mayanjun.myjack.core.annotation.Column;
import org.mayanjun.myjack.core.enums.DataType;

/**
 * 可后台编辑的实体
 */
public abstract class EditableEntity extends EntityBean {

	public EditableEntity() {
    }

	public EditableEntity(Long id) {
		super(id);
	}

	@Column(type = DataType.VARCHAR, length = "32", comment = "创建人")
	private String creator;

	@Column(type = DataType.VARCHAR, length = "32", comment = "最后一次编辑人")
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