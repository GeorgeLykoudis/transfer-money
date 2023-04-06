package com.example.transfermoney.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author George Lykoudis
 * @date 4/6/2023
 */
public class JsonParser<T> {

    public T loadJson(String fileName, Class<T> clazz) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream(fileName);
        return new ObjectMapper().readValue(is, clazz);
    }
}
