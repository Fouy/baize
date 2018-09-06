package com.moguhu.baize.service;

import com.moguhu.baize.common.concurrent.CommonThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.*;

/**
 * 线程服务
 *
 * @author xuefeihu
 */
@Service
public class CommonThreadService {

    private static final Logger logger = LoggerFactory.getLogger(CommonThreadService.class);

    private static ExecutorService threadPool;

    /************************************ 线程池相关 ************************************/

    /**
     * 池中所保存的线程数，包括空闲线程
     */
    @Value("${common.threadpool.es.coreSize}")
    private String ES_CORE_SIZE;

    /**
     * 池中允许的最大线程数
     */
    @Value("${common.threadpool.es.maxPoolSize}")
    private String ES_MAX_POOL_SIZE;

    /**
     * 当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间
     */
    @Value("${common.threadpool.es.keepAliveTime}")
    private String ES_KEEP_ALIVE_TIME;

    /**
     * 执行前用于保持任务的队列大小
     */
    @Value("${common.threadpool.es.queueSize}")
    private String ES_QUEUE_SIZE;

    // 加载大转盘线程池
    @PostConstruct
    public void init() {
        if (threadPool == null) {
            try {
                int corePoolSize = Integer.valueOf(ES_CORE_SIZE);
                int maxPoolSize = Integer.valueOf(ES_MAX_POOL_SIZE);
                int keepAliveTime = Integer.valueOf(ES_KEEP_ALIVE_TIME);
                int queueSize = Integer.valueOf(ES_KEEP_ALIVE_TIME);
                threadPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS,
                        new LinkedBlockingQueue<Runnable>(queueSize), new CommonThreadFactory("Common", ""),
                        new RejectedExecutionHandler() {
                            @Override
                            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                                if (!executor.isShutdown()) {
                                    try {
                                        executor.getQueue().put(r);
                                    } catch (InterruptedException e) {
                                        logger.error("When task queue is full, some bad things happened! ", e);
                                    }
                                }
                            }
                        });
            } catch (Exception e) {
                logger.error(" Common init thread pool failed ! ", e);
            }
        }
    }

    public Future<Long> submit(Callable<Long> task) {
        return threadPool.submit(task);
    }

    @PreDestroy
    public void destory() {
        if (threadPool != null) {
            try {
                threadPool.shutdown();
            } catch (Exception e) {
                logger.error(" Common executor service shutdown failed!", e);
            }
        }
    }

}
