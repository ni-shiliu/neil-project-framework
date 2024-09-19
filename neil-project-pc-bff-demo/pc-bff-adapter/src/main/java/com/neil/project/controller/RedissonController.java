package com.neil.project.controller;

import com.neil.project.common.BaseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * @author nihao
 * @date 2024/9/18
 */
@Tag(name = "分布式锁 - redisson")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/pc/redisson/")
public class RedissonController {

    private final RedissonClient redissonClient;

    private static final String LOCK_PREFIX = "REDISSON_KEY_";


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
