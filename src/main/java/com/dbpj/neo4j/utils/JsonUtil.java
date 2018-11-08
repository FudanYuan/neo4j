package com.dbpj.neo4j.utils;

/**
 * @Author: Jeremy
 * @Date: 2018/11/7 15:53
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil {

    public static String toJson(Object object) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(object);
    }
}