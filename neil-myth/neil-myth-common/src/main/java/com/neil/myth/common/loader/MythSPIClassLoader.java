package com.neil.myth.common.loader;

import com.neil.myth.annotation.MythSPI;
import com.neil.myth.common.exception.MythException;

import java.util.Objects;
import java.util.ServiceLoader;
import java.util.Spliterator;
import java.util.stream.StreamSupport;

/**
 * @author nihao
 * @date 2024/6/7
 */
public class MythSPIClassLoader<T> {

    private Class<T> clazz;

    private MythSPIClassLoader(Class<T> clazz) {
        this.clazz = clazz;
    }

    public static <T> MythSPIClassLoader<T> getMythSPIClassLoader(Class<T> clazz) {
        if (Objects.isNull(clazz)) {
            throw new MythException("getMythLoader error: clazz == null");
        }
        if (!clazz.isInterface()) {
            throw new MythException("getMythLoader error: clazz=" + clazz + "n ot interface!");
        }
        if (!withMythSPIAnnotation((clazz))) {
            throw new MythException("getMythLoader error: clazz.annotation.MythSPI" + clazz.getName() + "not exist");
        }
        return new MythSPIClassLoader<>(clazz);

    }

    private static <T> boolean withMythSPIAnnotation(Class<T> clazz) {
        return clazz.isAnnotationPresent(MythSPI.class);
    }

    public T getActivateMythSPIClazz(String value) {
        ServiceLoader<T> loader = ServiceLoader.load(clazz);
        Spliterator<T> spliterator = loader.spliterator();
        return StreamSupport.stream(spliterator,false)
                .filter(e -> e.getClass().getAnnotation(MythSPI.class).value().equals(value))
                .findFirst()
                .orElseThrow(() -> new MythException("please check your SPI config"));
    }

}
