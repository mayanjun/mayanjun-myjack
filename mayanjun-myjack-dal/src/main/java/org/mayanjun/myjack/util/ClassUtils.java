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

package org.mayanjun.myjack.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassUtils
 *
 * @author mayanjun(6/20/16)
 * @since 0.0.5
 */
public class ClassUtils {

    private ClassUtils() {
    }

    private static final Map<Class<?>, Map<String, Field>> FIELDS_CACHE;

    static {
        FIELDS_CACHE = new ConcurrentHashMap<Class<?>, Map<String, Field>>(
                new IdentityHashMap<Class<?>, Map<String, Field>>()
        );
    }

    public static Collection<Field> getAllFields(Class<?> cls) {
        return getAllFieldMap(cls).values();
    }

    public static Map<String, Field> getAllFieldMap(Class<?> cls) {
        Map<String, Field> fieldMap = FIELDS_CACHE.get(cls);
        if(fieldMap == null) {
            fieldMap = getAllInheritedFields(cls);
            FIELDS_CACHE.put(cls, fieldMap);
        }
        return fieldMap;
    }

    private static Map<String, Field> getAllInheritedFields(Class<?> cls) {
        Map<String, Field> map = new HashMap<String, Field>();
        if (cls == Object.class) return map;

        Map<String, Field> superMap = getAllInheritedFields(cls.getSuperclass());
        if (!superMap.isEmpty()) {
            map.putAll(superMap);
        }

        Field fields[] = cls.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field f : fields) {
                map.put(f.getName(), f);
            }
        }

        return map;
    }

    /**
     * Get field by name
     * @param cls
     * @param name
     * @return return null if no field specified by name found
     */
    public static Field getField(Class<?> cls, String name) {
        Map<String, Field> fieldMap = getAllFieldMap(cls);
        return fieldMap.get(name);
    }

    public static Class<?> getFirstParameterizedType(Class<?> beanType) {
        Class cls = beanType;
        Type t = cls.getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType)t;
        Type[] ats = pt.getActualTypeArguments();
        return (Class)ats[0];
    }

}
