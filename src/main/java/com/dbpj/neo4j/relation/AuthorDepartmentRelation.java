package com.dbpj.neo4j.relation;

import com.dbpj.neo4j.node.Author;
import com.dbpj.neo4j.node.Department;
import lombok.Data;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/**
 * @Author: Jeremy
 * @Date: 2018/11/3 21:42
 */
@RelationshipEntity(type = "work_in")
@Data
public class AuthorDepartmentRelation {
    @GraphId
    private Long id;

    @StartNode
    private Author author;

    @EndNode
    private Department department;

    /**
     * 开始工作年份
     */
    private Integer sYear;

    /**
     * 结束工作年份
     */
    private Integer eYear;
}
