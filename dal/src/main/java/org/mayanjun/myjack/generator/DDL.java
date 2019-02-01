package org.mayanjun.myjack.generator;

import org.mayanjun.myjack.core.annotation.Table;
import org.mayanjun.myjack.core.entity.PersistableEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DDLGenerator
 *
 * @author mayanjun(14/11/2016)
 *
 * @since 0.0.4
 */
public class DDL {

	private static final Logger LOG = LoggerFactory.getLogger(DDL.class);
	private static MysqlSchemeGenerator GENERATOR = new MysqlSchemeGenerator(true);
	private static TypeFilter FILTER = new AnnotationTypeFilter(Table.class);
	private static ResourcePatternResolver RESOLVER = new PathMatchingResourcePatternResolver();
	private static MetadataReaderFactory READER_FACTORY = new CachingMetadataReaderFactory(RESOLVER);


	public static List<Resource> parseResources(String... packageName) throws Exception {
		List<Resource> list = new ArrayList<Resource>();
		for(String pkg : packageName) {
			String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(pkg) + "/**/*.class";
			Resource resources[] = RESOLVER.getResources(pattern);
			list.addAll(Arrays.asList(resources));
		}
		return list;
	}


	public static String ddl(Class<? extends PersistableEntity> clazz, String tableName, boolean genDrop) throws Exception {
		return GENERATOR.generate(clazz, tableName, genDrop);
	}

	public static void ddl(File outFile, boolean includeJar, String... packageName) throws Exception {
		ddl(outFile, includeJar, true, packageName);
	}

	public static void ddl(File outFile, boolean includeJar, boolean genDrop, String... packageName) throws Exception {
		List<Resource> resources = parseResources(packageName);
		FileWriter writer;
		try {
			writer = new FileWriter(outFile);
			for (Resource r : resources) {
				if(!includeJar) {
					URI uri = r.getURI();
					String scheme = uri.getScheme();
					if("jar".equals(scheme)) continue;
				}

				MetadataReader reader = READER_FACTORY.getMetadataReader(r);
				boolean matched = FILTER.match(reader, READER_FACTORY);
				if (matched) {
					String className = reader.getClassMetadata().getClassName();
					Class<?> cls = Class.forName(className);
					LOG.info("Generating: " + className);
					writer.write(GENERATOR.generate(cls, null, genDrop));
				}
			}
			writer.flush();
			LOG.info("DDL write success!");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
