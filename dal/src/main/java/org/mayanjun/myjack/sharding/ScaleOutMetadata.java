package org.mayanjun.myjack.sharding;

import org.mayanjun.myjack.core.annotation.Column;
import org.mayanjun.myjack.core.annotation.Index;
import org.mayanjun.myjack.core.annotation.IndexColumn;
import org.mayanjun.myjack.core.annotation.Table;
import org.mayanjun.myjack.core.entity.EntityBean;
import org.mayanjun.myjack.core.enums.DataType;

/**
 * ScaleOutMetadata
 *
 * @author mayanjun(23/09/2016)
 */
@Table(value = "t_scaleout_metadata",
		indexes = {
				@Index(value = "idx_systemId", columns = @IndexColumn("systemId")),
				@Index(value = "idx_entityName", columns = @IndexColumn("entityName"))
		},
		comment = "分库分表规则元数据")
public class ScaleOutMetadata extends EntityBean {

	public ScaleOutMetadata() {
	}

	public ScaleOutMetadata(long id) {
		super(id);
	}

	public ScaleOutMetadata(long scale, String entityName, String matrixJson) {
		this(scale, entityName, matrixJson, null);
	}

	public ScaleOutMetadata(long scale, String entityName, String matrixJson, String systemId) {
		this.scale = scale;
		this.entityName = entityName;
		this.matrixJson = matrixJson;
		this.systemId = systemId;
	}

	/**
	 * 数据规模，或者数据ID，表示的是一个阶梯
	 */
	@Column(type = DataType.BIGINT, comment = "数据规模")
	private Long scale;

	@Column(type = DataType.VARCHAR, length = "32", comment = "系统标识")
	 private String systemId;

	/**
	 * 各个实体的分库分表规则可以不同，用该值来标记一个实体, 建议存储实体类的 CanonicalName
	 */
	@Column(type = DataType.VARCHAR, length = "64", comment = "实体名称")
	private String entityName;

	/**
	 * 存储一个数据库名称和表名称的矩阵数据，以JSON格式存储
	 */
	@Column(type = DataType.MEDIUMTEXT, comment = "分库分表名称矩阵")
	private String matrixJson;

	public Long getScale() {
		return scale;
	}

	public void setScale(Long scale) {
		this.scale = scale;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getMatrixJson() {
		return matrixJson;
	}

	public void setMatrixJson(String matrixJson) {
		this.matrixJson = matrixJson;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	@Override
	public String toString() {
		return "ScaleOutMetadata{" + "scale=" + scale + ", systemId='" + systemId + '\'' + ", entityName='" + entityName + '\'' + ", matrixJson='" + matrixJson + '\'' + '}';
	}
}