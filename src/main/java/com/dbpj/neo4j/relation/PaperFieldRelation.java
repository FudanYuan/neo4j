package com.dbpj.neo4j.relation;

import com.dbpj.neo4j.node.Field;
import com.dbpj.neo4j.node.Paper;
import lombok.Data;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/**
 * @Author: Jeremy
 * @Date: 2018/11/7 13:44
 */
@RelationshipEntity(type = "involve")
@Data
public class PaperFieldRelation {
    @GraphId
    private Long id;

    /**
     * 论文
     */
    @StartNode
    private Paper paper;

    /**
     * 涉及的领域
     */
    @EndNode
    private Field field;
}
