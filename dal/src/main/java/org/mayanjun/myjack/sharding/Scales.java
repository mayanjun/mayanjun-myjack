package org.mayanjun.myjack.sharding;

import org.mayanjun.myjack.IdWorker;
import org.mayanjun.myjack.idworker.IdWorkerFactory;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Scales
 *
 * @author mayanjun(18/10/2016)
 */
public class Scales {

	private static final long ID_WORKER_TIMESTAMP_SHIFT = 14;
	private static final IdWorker ID_WORKER = IdWorkerFactory.create(0);
	private static final long MINUTES30 = TimeUnit.MINUTES.toMillis(30);

	private Scales() {
	}

	/**
	 * 生成一个未来的某个时间的scale，
	 * @param future 未来某个时间的时间戳，必须至少在30分钟以后
	 * @return
	 */
	public static long scale(long future) {
		long diff = future - System.currentTimeMillis();
		Assert.isTrue(diff > MINUTES30, "只能计算30分钟以后的scale");
		long id = ID_WORKER.next();
		long time = id >> ID_WORKER_TIMESTAMP_SHIFT;
		time += diff;
		long ok = time << ID_WORKER_TIMESTAMP_SHIFT;
		return ok;
	}

	/**
	 * 创建一个 {@link ScaleOutMetadata} 对象
	 * @param entityName 实体名称
	 * @param systemId 系统ID
	 * @param matrix 参见 {@link NameMatrix#serialize(String)}
	 * @param future 参见 {@link #scale(long)}
	 * @return ScaleOutMetadata对象
	 */
	public static ScaleOutMetadata createScaleOutMetadata(
			String entityName,
			String systemId,
			String matrix,
			long future) {
		long scale = scale(future);
		String matrixJson = NameMatrix.serialize(matrix);
		return new ScaleOutMetadata(scale, entityName, matrixJson, systemId);
	}

	public static ScaleOutMetadata createScaleOutMetadata(String entityName, String systemId, String matrix) {
		long scale = 0;
		String matrixJson = NameMatrix.serialize(matrix);
		return new ScaleOutMetadata(scale, entityName, matrixJson, systemId);
	}

	public static String toSQL(ScaleOutMetadata metadata) {
		long id = System.currentTimeMillis() - (long)(Math.random()*1000000);
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		return "INSERT INTO `t_scaleout_metadata`(`id`,`scale`,`createdTime`,`matrixJson`,`modifiedTime`, `systemId`, `entityName`)" +
				" VALUES(" + id + "," + metadata.getScale() + ",'" + date + "','" + metadata.getMatrixJson() + "', '" + date + "','" + metadata.getSystemId() + "', '" + metadata.getEntityName() +"');";
	}

}
