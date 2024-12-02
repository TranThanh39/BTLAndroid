package com.haruma.app.service;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class JsonHelper {

    public static void writeJson(String filePath, Object object) throws RuntimeException {
        Gson gson = new Gson();
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(object, writer);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T readJson(String filePath, Class<T> clazz) throws RuntimeException {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
