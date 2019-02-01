package org.mayanjun.myjack.core.entity;

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
	public P getId();
	public void setId(P id);

	/**
	 * Returns the created time
	 *
	 * @return the date that the entity created
	 */
	public Date getCreatedTime();
	public void setCreatedTime(Date createdTime);

	/**
	 * Returns the modified time
	 *
	 * @return the date of the entity modified
	 */
	public Date getModifiedTime();
	public void setModifiedTime(Date modifiedTime);
}
