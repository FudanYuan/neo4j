package com.dbpj.neo4j.vo;

import lombok.Data;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

/**
 * @Author: Jeremy
 * @Date: 2018/12/11 13:04
 */
@Data
public class Neo4jGraphVO {
    private String type;

    private List<TreeMap> categories;

    private List<TreeMap> nodes;

    private List<TreeMap> links;

    private Long time;

    public Neo4jGraphVO(){
        this.categories = getCategories(Arrays.asList("paper", "author", "conference", "field", "department"));
    }

    public Neo4jGraphVO(String type){
        this.type = type;
        this.categories = getCategories(Arrays.asList("paper", "author", "conference", "field", "department"));
    }

    private static List getCategories(List<String> nodes){
        List<TreeMap> maps = new ArrayList<>();
        for (String node : nodes){
            TreeMap<String, Object> map = new TreeMap<>();
            map.put("name", node);
            map.put("keyword", new TreeMap<>());
            map.put("base", "");
            maps.add(map);
        }
        return maps;
    }
}
