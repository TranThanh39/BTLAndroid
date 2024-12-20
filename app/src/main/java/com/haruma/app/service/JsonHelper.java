package com.haruma.app.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.Map;

public class JsonHelper {

    public static void writeJson(String filePath, Object object) throws RuntimeException {
        Gson gson = new Gson();
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(object, writer);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Map<String, Object>> readJson(String filePath) throws RuntimeException {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, new TypeToken<List<Map<String, Object>>>(){}.getType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
