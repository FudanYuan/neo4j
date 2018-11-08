package com.dbpj.neo4j.node;

import lombok.Data;
import lombok.Generated;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * @Author: Jeremy
 * @Date: 2018/11/3 19:33
 */
@NodeEntity(label = "field")
@Data
public class Field {

    @GraphId
    @Generated
    private Long id;

    /**
     * 研究领域
     */
    private String fName;
}
