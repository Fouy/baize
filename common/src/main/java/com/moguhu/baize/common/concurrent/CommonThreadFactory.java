package com.moguhu.baize.common.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程工厂
 *
 * @author xuefeihu
 */
public class CommonThreadFactory implements ThreadFactory {

    private static final AtomicInteger poolNumber = new AtomicInteger(1);

    private final ThreadGroup group;

    private final AtomicInteger threadNumber = new AtomicInteger(1);

    private final String namePrefix;

    /**
     * 模块名
     */
    private String moduleName;

    /**
     * 功能名
     */
    private String functionName;

    public CommonThreadFactory(String moduleName, String functionName) {
        this.moduleName = moduleName == null ? "" : moduleName;
        this.functionName = functionName == null ? "" : functionName;
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        // 在线程名上加上模块名标示，方便调试
        namePrefix = this.moduleName + "-" + this.functionName + "-pool-" + poolNumber.getAndIncrement() + "-thread-";
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
        if (t.isDaemon())
            t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }

}
