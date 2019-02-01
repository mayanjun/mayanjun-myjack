package org.mayanjun.myjack.support;

import org.springframework.beans.factory.FactoryBean;

import java.util.Properties;

/**
 * @author mayanjun
 * @since 2018/7/13
 */
public class PropertiesFactoryBean implements FactoryBean<Properties> {

    private Properties connectionProperties = new Properties();

    @Override
    public Properties getObject() {
        return connectionProperties;
    }

    @Override
    public Class<?> getObjectType() {
        return Properties.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setConnectionProperties(final String connectionProperties) {
        if (connectionProperties == null) {
            throw new NullPointerException("connectionProperties is null");
        }

        final String[] entries = connectionProperties.split(";");
        final Properties properties = new Properties();
        for (final String entry : entries) {
            if (entry.length() > 0) {
                final int index = entry.indexOf('=');
                if (index > 0) {
                    final String name = entry.substring(0, index);
                    final String value = entry.substring(index + 1);
                    properties.setProperty(name, value);
                } else {
                    // no value is empty string which is how java.util.Properties works
                    properties.setProperty(entry, "");
                }
            }
        }
        this.connectionProperties = properties;
    }
}
