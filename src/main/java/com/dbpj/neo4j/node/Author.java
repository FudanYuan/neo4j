package com.dbpj.neo4j.node;

import lombok.Data;
import lombok.Generated;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * @Author: Jeremy
 * @Date: 2018/10/29 21:52
 */

@NodeEntity(label = "author")
@Data
public class Author {
    /**
     * 主键
     */
    @GraphId
    @Generated
    private Long id;

    /**
     * 姓名
     */
    private String aName;

    /**
     * 个人主页url
     */
    private String aUrl;
}
