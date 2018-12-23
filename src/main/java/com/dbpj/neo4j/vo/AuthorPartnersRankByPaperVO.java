package com.dbpj.neo4j.vo;

import com.dbpj.neo4j.node.Author;
import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

/**
 * @Author: Jeremy
 * @Date: 2018/12/23 17:31
 */
@QueryResult
@Data
public class AuthorPartnersRankByPaperVO {
    /**
     * 作者1
     */
    Author author1;

    /**
     * 作者2
     */
    Author author2;

    /**
     * 论文数量
     */
    Integer paperCount;
}
