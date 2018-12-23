package com.dbpj.neo4j.vo;

import com.dbpj.neo4j.node.Author;
import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

/**
 * @Author: Jeremy
 * @Date: 2018/12/23 17:12
 */
@QueryResult
@Data
public class AuthorRankByPaperVO {
    /**
     * 作者
     */
    Author author;

    /**
     * 论文数量
     */
    Integer paperCount;
}
