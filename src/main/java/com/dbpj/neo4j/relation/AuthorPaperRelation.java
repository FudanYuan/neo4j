package com.dbpj.neo4j.relation;

import com.dbpj.neo4j.node.Author;
import com.dbpj.neo4j.node.Paper;
import lombok.Data;
import lombok.Generated;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/**
 * @Author: Jeremy
 * @Date: 2018/11/7 08:54
 */
@RelationshipEntity(type = "publish")
@Data
public class AuthorPaperRelation {

    @GraphId
    private Long id;

    /**
     * 论文作者
     */
    @StartNode
    private Author author;

    /**
     * 发表论文
     */
    @EndNode
    private Paper paper;

    /**
     * 第几作者
     */
    private Integer aIndex;
}
