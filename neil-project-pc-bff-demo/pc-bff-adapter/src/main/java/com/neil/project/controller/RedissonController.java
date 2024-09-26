package com.neil.project.controller;

import com.neil.project.common.BaseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.redisson.api.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author nihao
 * @date 2024/9/18
 */
@Tag(name = "redisson")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/pc/redisson/")
public class RedissonController {

    private final RedissonClient redissonClient;

    private final RRateLimiter rateLimiter;

    private static final String LOCK_PREFIX = "REDISSON_KEY_";

    @Operation(summary = "bucket")
    @GetMapping("bucket")
    public BaseResult<Void> bucket(@RequestParam("orderNo") String orderNo) {
        RBucket<Object> rBucket = redissonClient.getBucket("order");
        rBucket.set(orderNo);
        Object andSet1 = rBucket.getAndSet(orderNo);
        rBucket.delete();

        RBucket<Object> expireOrder = redissonClient.getBucket("expire_order1");
        expireOrder.set("expire_" + orderNo, 5, TimeUnit.SECONDS);
        Object o = expireOrder.get();
        return BaseResult.success();
    }

    @Operation(summary = "atomic")
    @GetMapping("atomic")
    public BaseResult<Void> atomic() {
        RAtomicLong atomic = redissonClient.getAtomicLong("atomic");
        long a = atomic.incrementAndGet();
        System.out.println(a);
        return BaseResult.success();
    }

    @Operation(summary = "limiter")
    @GetMapping("limiter")
    public BaseResult<Void> limiter() {
        if (!rateLimiter.tryAcquire()) {
            return BaseResult.fail("请求过于频繁，请稍后后重试。");
        }
        return BaseResult.success();
    }


    @Operation(summary = "lock")
    @GetMapping("lock")
    public BaseResult<Void> lock(@RequestParam("orderNo") String orderNo) {
        RLock lock = redissonClient.getLock(LOCK_PREFIX + orderNo);
        // 尝试获取锁，一直等待，直到获取到锁
        lock.lock();
        // 尝试获取锁，一直等待，直到获取到锁，leaseTime后锁自动失效
//        lock.lock(10, TimeUnit.SECONDS);
        try {
            // 执行业务
            System.out.println("lock acquired for orderNo: " + orderNo);
            Thread.sleep(10000L);
        } catch (Exception e) {
            return BaseResult.fail("执行业务失败。");
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return BaseResult.success();
    }

    @Operation(summary = "tryLock")
    @GetMapping("tryLock")
    public BaseResult<Void> tryLock(@RequestParam("orderNo") String orderNo) {
        RLock lock = redissonClient.getLock(LOCK_PREFIX + orderNo);
        try {
            // 尝试获取锁，不等待， 未获取到锁则返回false
//            if (lock.tryLock()) {
            // 尝试获取锁，最多等待5S
//            if (lock.tryLock(5, TimeUnit.SECONDS)) {
            // 尝试获取锁，最多等待 5 秒，最长等待 10 秒
            if (lock.tryLock(5, 10, TimeUnit.SECONDS)) {
                // 执行业务
                System.out.println("lock acquired for orderNo: " + orderNo);
                Thread.sleep(10000L);
            } else {
                return BaseResult.fail("锁已被占用，请稍后重试");
            }
        } catch (Exception e) {
            return BaseResult.fail("执行业务失败。");
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return BaseResult.success();
    }

}
