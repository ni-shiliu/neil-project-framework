package com.neil.myth.common.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.io.Input;
import com.neil.myth.annotation.MythSPI;
import com.neil.myth.common.exception.MythException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author nihao
 * @date 2024/6/7
 */
@MythSPI("kryo")
public class KryoSerializer implements Serializer {

    @Override
    public byte[] serialize(final Object obj) throws MythException {
        byte[] bytes;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); Output output = new Output(outputStream)) {
            //获取kryo对象
            Kryo kryo = new Kryo();
            kryo.writeObject(output, obj);
            bytes = output.toBytes();
            output.flush();
        } catch (IOException ex) {
            throw new MythException("kryo serialize error" + ex.getMessage());
        }
        return bytes;
    }

    @Override
    public <T> T deSerialize(final byte[] param, final Class<T> clazz) throws MythException {
        T object;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(param)) {
            Kryo kryo = new Kryo();
            Input input = new Input(inputStream);
            object = kryo.readObject(input, clazz);
            input.close();
        } catch (IOException e) {
            throw new MythException("kryo deSerialize error" + e.getMessage());
        }
        return object;
    }
}
