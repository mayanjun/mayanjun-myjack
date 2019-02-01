package org.mayanjun.myjack.zookeeper;

import org.springframework.beans.factory.annotation.Required;

/**
 * ZooKeeperBase
 *
 * @author mayanjun(28/09/2016)
 */
public class ZooKeeperBase {

	protected String namespace;

	@Required
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String clearNodePath(String path) {
		if(path != null) {
			return path.replaceAll("/{2,}", "/");
		}
		return path;
	}



}
