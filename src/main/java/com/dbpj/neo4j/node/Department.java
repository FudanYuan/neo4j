package com.dbpj.neo4j.node;

import lombok.Data;
import lombok.Generated;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * @Author: Jeremy
 * @Date: 2018/11/3 18:37
 */
@NodeEntity(label = "department")
@Data
public class Department {
    @GraphId
    @Generated
    private Long id;

    /**
     * 单位名称
     */
    private String dName;

    /**
     * 单位地址
     */
    private String dAddress;
}
