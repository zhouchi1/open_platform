package com.zhouzhou.cloud.common.utils;


import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-1-25
 * @Description: 分布式雪花算法UUID生成
 */
public class SnowflakeIdGeneratorUtil {

    /**
     * 数据中心概念：
     * 当项目上线后为了保证高可用容灾 需要部署在不同的地域 防止一整个区域的服务器因为火灾、地震等原因集体宕机导致服务不可用的情况
     * 例如：北京、上海、广州、深圳等地的机房
     *
     * 机器概念：
     * 机器相当于是数据中心的子集 一个数据中心可以有多个机器
     * 例如：北京-1服务器、北京-2服务器、上海-1服务器、上海-2服务器、广州-1服务器、广州-2服务器、深圳-1服务器、深圳-2服务器
     */

    // 开始时间戳 (2020-01-01 00:00:00)
    private static final long START_TIMESTAMP = 1577836800000L;

    // 每一部分的位数
    private static final long SEQUENCE_BIT = 12; // 序列号占用的位数
    private static final long MACHINE_BIT = 5;  // 机器 ID 占用的位数
    private static final long DATACENTER_BIT = 5; // 数据中心占用的位数

    // 每一部分的最大值
    private static final long MAX_DATACENTER_NUM = ~(-1L << DATACENTER_BIT);
    private static final long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);

    // 每一部分向左的位移
    private static final long MACHINE_SHIFT = SEQUENCE_BIT;
    private static final long DATACENTER_SHIFT = SEQUENCE_BIT + MACHINE_BIT;
    private static final long TIMESTAMP_SHIFT = DATACENTER_SHIFT + DATACENTER_BIT;

    private final long datacenterId; // 数据中心 ID
    private final long machineId;    // 机器 ID
    private long sequence = 0L; // 当前序列号
    private long lastTimestamp = -1L; // 上次生成 ID 的时间戳

    // 构造方法
    public SnowflakeIdGeneratorUtil(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("Datacenter ID can't be greater than " + MAX_DATACENTER_NUM + " or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("Machine ID can't be greater than " + MAX_MACHINE_NUM + " or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    // 生成下一个 ID
    public synchronized long nextId() {
        long currentTimestamp = System.currentTimeMillis();

        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate ID");
        }

        if (currentTimestamp == lastTimestamp) {
            // 同一毫秒内生成 ID
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0L) {
                // 序列号达到最大值，等待下一毫秒
                currentTimestamp = getNextMillis();
            }
        } else {
            // 不同毫秒，重置序列号
            sequence = 0L;
        }

        lastTimestamp = currentTimestamp;

        // 拼接 ID 并返回
        return ((currentTimestamp - START_TIMESTAMP) << TIMESTAMP_SHIFT) // 时间戳部分
                | (datacenterId << DATACENTER_SHIFT)                    // 数据中心部分
                | (machineId << MACHINE_SHIFT)                          // 机器 ID 部分
                | sequence;                                             // 序列号部分
    }

    // 等待直到下一毫秒
    private long getNextMillis() {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    // 自动生成机器 ID 和数据中心 ID（可选）
    public static SnowflakeIdGeneratorUtil createWithAutoConfig() throws Exception {
        long machineId = getMachineId();
        long datacenterId = getDatacenterId();
        return new SnowflakeIdGeneratorUtil(datacenterId, machineId);
    }

    // 通过 MAC 地址计算机器 ID
    private static long getMachineId() throws Exception {
        byte[] mac = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
        if (mac == null) {
            return 1L;
        }
        long id = 0;
        for (int i = 0; i < mac.length; i++) {
            id += (long) (mac[i] & 0xFF) << (8 * i);
        }
        return id & MAX_MACHINE_NUM;
    }

    // 数据中心 ID 可根据环境变量或其他策略生成
    private static long getDatacenterId() {
        return 1L; // 默认返回 1，可根据实际情况调整
    }
}
