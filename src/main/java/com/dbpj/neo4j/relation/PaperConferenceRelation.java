package com.dbpj.neo4j.relation;

import com.dbpj.neo4j.node.Conference;
import com.dbpj.neo4j.node.Paper;
import lombok.Data;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/**
 * @Author: Jeremy
 * @Date: 2018/11/7 14:15
 */
@RelationshipEntity(type = "belong_to")
@Data
public class PaperConferenceRelation {
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
    private Conference conference;
}
