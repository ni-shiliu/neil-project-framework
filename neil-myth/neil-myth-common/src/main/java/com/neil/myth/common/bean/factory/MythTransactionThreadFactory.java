package com.neil.myth.common.bean.factory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author nihao
 * @date 2024/6/11
 */
public class MythTransactionThreadFactory implements ThreadFactory {

    private boolean daemon;

    private final ThreadGroup THREAD_GROUP = new ThreadGroup("MythTransaction");

    private final AtomicLong threadNumber = new AtomicLong(1);

    private final String namePrefix;

    private MythTransactionThreadFactory(final String namePrefix, final boolean daemon) {
        this.namePrefix = namePrefix;
        this.daemon = daemon;
    }

    public static ThreadFactory create(final String namePrefix, final boolean daemon) {
        return new MythTransactionThreadFactory(namePrefix, daemon);
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(THREAD_GROUP, runnable,
                THREAD_GROUP.getName() + "-" + namePrefix + "-" + threadNumber.getAndIncrement());
        thread.setDaemon(daemon);
        if (thread.getPriority() != Thread.NORM_PRIORITY) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }
}
