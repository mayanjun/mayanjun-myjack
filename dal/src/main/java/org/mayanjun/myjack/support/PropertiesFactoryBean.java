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

package org.mayanjun.myjack.support;

import org.springframework.beans.factory.FactoryBean;

import java.util.Properties;

/**
 * Convert comma separated key-value pair to {@link Properties}
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
