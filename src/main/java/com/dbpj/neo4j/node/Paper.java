package com.dbpj.neo4j.node;

import lombok.Data;
import lombok.Generated;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * @Author: Jeremy
 * @Date: 2018/11/3 14:45
 */

@NodeEntity(label = "paper")
@Data
public class Paper {
    /**
     * 主键
     */
    @GraphId
    @Generated
    private Long id;

    /**
     * 论文题目
     */
    private String pTitle;

    /**
     * 论文摘要
     */
    private String pAbstract;

    /**
     * 论文页码
     */
    private Integer pPage;

    /**
     * 引用次数
     */
    private Integer pCitation;

    /**
     * 发表年份
     */
    private Integer pYear;
}
