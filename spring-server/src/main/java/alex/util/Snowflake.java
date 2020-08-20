package alex.util;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.io.Serializable;

/**
 * Created by Alex.Sun on 2018-09-27 12:46.
 */
public final class Snowflake implements Serializable {
    public static final FastDateFormat TIMESTAMP_FORMAT_COMPACT = FastDateFormat.getInstance("yyyyMMddHHmmssSSS");
    private static final long serialVersionUID = 1;

    public static final Snowflake DEFAULT = Snowflake.getInstance(0);

    @Getter
    public static class ID implements Serializable {
        private static final long serialVersionUID = 1;

        //时间戳
        private final long timestamp;

        //节点ID
        private final int workerId;

        //计数器
        private final int count;

        public ID(long timestamp, int workerId, int count) {
            this.timestamp = timestamp;
            this.workerId = workerId;
            this.count = count;
        }

        @Override
        public String toString() {
            return toString(false);
        }

        public String toString(boolean fixedLength) {
            if (fixedLength) {
                return TIMESTAMP_FORMAT_COMPACT.format(timestamp) + StringUtils.leftPad(String.valueOf(workerId), MAX_WORKER_ID_LEN, '0') + StringUtils.leftPad(String.valueOf(count), MAX_COUNTER_LEN, '0');
            } else {
                return TIMESTAMP_FORMAT_COMPACT.format(timestamp) + '-' + String.valueOf(workerId) + '-' + String.valueOf(count);
            }
        }
    }


    private final static long EPOCH = 1451577600000L;//2016-01-01 00:00:00
    private final static int WORKER_ID_BITS = 8;
    private final static int COUNTER_BITS = 10;
    private final static long TIMESTAMP_SHIFT = (long) (WORKER_ID_BITS + COUNTER_BITS);
    private final static long WORKER_ID_MASK = (long) ((1 << WORKER_ID_BITS) - 1);
    private final static long COUNTER_MASK = (long) ((1 << COUNTER_BITS) - 1);
    private final static int MAX_WORKER_ID = Math.toIntExact(WORKER_ID_MASK);
    private final static int MAX_COUNTER = Math.toIntExact(COUNTER_MASK);
    private final static int MAX_WORKER_ID_LEN = String.valueOf(MAX_WORKER_ID).length();
    private final static int MAX_COUNTER_LEN = String.valueOf(MAX_COUNTER).length();


    private final int workerId;
    private int counter = 0;
    private long lastTimestamp = -1L;

    private Snowflake(int workerId) {
        this.workerId = workerId;
        if (workerId < 0 || workerId > MAX_WORKER_ID) {
            throw new IllegalArgumentException("Invalid workId: " + workerId + " [0 ~ " + MAX_WORKER_ID + "]");
        }
    }

    public synchronized long nextId() {
        long timestamp = now();
        if (timestamp > lastTimestamp) {
            counter = 0;
            lastTimestamp = timestamp;
        } else if (lastTimestamp > timestamp) {
            throw new IllegalStateException("Clock moved backwards, Refusing to generate id for " + (lastTimestamp - timestamp) + " milliseconds");
        }
        long id = timestamp - EPOCH << TIMESTAMP_SHIFT | workerId << COUNTER_BITS | counter++ & COUNTER_MASK;
        if (id < 0) throw new IllegalStateException("ID overflow!");
        return id;
    }

    private long now() {
        return System.currentTimeMillis();//+ 1024 * 365 * DateUtils.MILLIS_PER_DAY;
    }

    public static Snowflake getInstance(int workerId) {
        return new Snowflake(workerId);
    }

    public static long toTimeMillis(long id) {
        return (id >>> TIMESTAMP_SHIFT) + EPOCH;
    }

    public static ID toID(long id) {
        return new ID(toTimeMillis(id), Math.toIntExact((id >>> COUNTER_BITS) & WORKER_ID_MASK), Math.toIntExact(id & COUNTER_MASK));
    }

    public static long fromID(ID value) {
        long id = value.timestamp - EPOCH << TIMESTAMP_SHIFT | value.workerId << COUNTER_BITS | value.count & COUNTER_MASK;
        if (id < 0) throw new IllegalStateException("ID overflow!");
        return id;
    }

    public static void main(String[] args) {
        Snowflake snowflake = Snowflake.getInstance(255);
        for (int i = 0; i < 1000; i++) {
            long id = snowflake.nextId();
            ID value = Snowflake.toID(id);
            System.out.println(id + " -> " + value);
            long id2 = Snowflake.fromID(value);
            System.out.println(id2);
            if (id != id2) throw new IllegalStateException("not match");
        }

        System.out.println("now = [" + System.currentTimeMillis() + "]");
        System.out.println("won = [" + Snowflake.toTimeMillis(snowflake.nextId()) + "]");

        System.out.println("max=[" + Long.MAX_VALUE + "]");
    }
}
