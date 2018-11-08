package com.dbpj.neo4j.node;

import lombok.Data;
import lombok.Generated;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * @Author: Jeremy
 * @Date: 2018/11/3 20:12
 */

@NodeEntity(label = "conference")
@Data
public class Conference {

    @GraphId
    @Generated
    private Long id;

    /**
     * 会议名称
     */
    private String cName;
}
