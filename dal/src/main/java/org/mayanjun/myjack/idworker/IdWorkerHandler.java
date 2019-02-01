package org.mayanjun.myjack.idworker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * IdWorkerHandler
 *
 * @author mayanjun(10/10/15)
 */
class IdWorkerHandler {

    private static final Logger LOG = LoggerFactory.getLogger(IdWorkerHandler.class);

    public static final int MIN_WORKER_INDEX = 0;
    public static final int MAX_WORKER_INDEX = 0x0F;

    public static final int WORKER_LENGTH = MAX_WORKER_INDEX + 1;

    private IdGenerator[] generators;
    private AtomicInteger indexCounter = new AtomicInteger(0);

    /**
     * By default user all 16 indexes
     */
    public IdWorkerHandler() {
        setWorkerIndexes(null);
    }

    public IdWorkerHandler(int ... indexes) {
        setWorkerIndexes(indexes);
    }

    //@Required
    private void setWorkerIndexes(int... workerIndexes) {
        int indexes[];
        if(workerIndexes == null || workerIndexes.length == 0) indexes = newSequence();
        else indexes = newSequence(workerIndexes);

        Map<Integer, IdGenerator> generatorMap = new HashMap<Integer, IdGenerator>();

        generators = new IdGenerator[WORKER_LENGTH];

        for(int i = 0; i < WORKER_LENGTH; i++) {
            int index = indexes[i];
            IdGenerator generator = generatorMap.get(index);
            if(generator == null) {
                generator = new IdGenerator(index);
                generatorMap.put(index, generator);
            }
            generators[i] = generator;
        }
        LOG.info("Id generator initialized");
    }

    private int [] newSequence(int ... source) {
        int x[] = new int[WORKER_LENGTH];
        int len = source.length;
        for(int i = 0, j =0; i < WORKER_LENGTH; i++, j++) {
            if(j >= len) j = 0;
            if(source[j] >= WORKER_LENGTH) throw new IllegalArgumentException("Id worker index must less than " + WORKER_LENGTH + ", but got " + source[j]);
            x[i] = source[j];
        }
        LOG.info("Using indexes:" + toString(source));
        return x;
    }

    private int [] newSequence() {
        int x[] = new int[WORKER_LENGTH];
        for(int i = 0; i < WORKER_LENGTH; i++) x[i] = i;
        LOG.info("Using indexes:" + toString(x));
        return x;
    }

    private int nextWorkerIndex() {
        return indexCounter.incrementAndGet() & MAX_WORKER_INDEX;
    }

    public long nextId() {
        IdGenerator gen = generators[nextWorkerIndex()];
        return gen.nextId();
    }

    private String toString(int array[]) {
        if(array == null || array.length == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for(int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if(i < array.length - 1) sb.append(',');
        }
        sb.append(']');
        return sb.toString();
    }

    /**
     * The core code to generate id
     */
    private static class IdGenerator {
        private final long workerId;
        private long sequence = 0L;
        private final static long workerIdBits = 4L;
        public final static long maxWorkerId = -1L ^ -1L << workerIdBits;
        private final static long sequenceBits = 10L;

        private final static long workerIdShift = sequenceBits;
        private final static long timestampLeftShift = sequenceBits + workerIdBits;
        public final static long sequenceMask = -1L ^ -1L << sequenceBits;

        private int vibrance = -1;

        private long lastTimestamp = -1L;

        public IdGenerator(final long workerId) {
            super();
            if (workerId > maxWorkerId || workerId < 0) {
                throw new IllegalArgumentException(String.format(
                        "worker Id can't be greater than %d or less than 0",
                        maxWorkerId));
            }
            this.workerId = workerId;
        }

        public synchronized long nextId() {
            long timestamp = System.currentTimeMillis();
            if (this.lastTimestamp == timestamp) {
                this.sequence = (this.sequence + 1) & sequenceMask;
                if (this.sequence == 0) {
                    timestamp = this.tillNextMillis(this.lastTimestamp);
                }
            } else {
                this.sequence = (vibrance = ~vibrance & 1);
            }
            if (timestamp < this.lastTimestamp) {
                try {
                    throw new Exception(
                            String.format(
                                    "Clock moved backwards.  Refusing to generate id for %d milliseconds",
                                    this.lastTimestamp - timestamp));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            this.lastTimestamp = timestamp;
            long nextId = ((timestamp - IdWorkerFactory.EPOCH << timestampLeftShift))
                    | (this.workerId << IdGenerator.workerIdShift) | (this.sequence);
            return nextId;
        }

        private long tillNextMillis(final long lastTimestamp) {
            long timestamp = System.currentTimeMillis();
            while (timestamp <= lastTimestamp) {
                timestamp = System.currentTimeMillis();
            }
            return timestamp;
        }
    }
}
