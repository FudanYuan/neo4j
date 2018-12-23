package com.dbpj.neo4j.repository;

import com.dbpj.neo4j.node.Author;
import com.dbpj.neo4j.vo.AuthorPartnersRankByPaperVO;
import com.dbpj.neo4j.vo.AuthorRankByPaperVO;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.annotation.QueryResult;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Author: Jeremy
 * @Date: 2018/10/29 21:58
 */
public interface AuthorRepository extends GraphRepository<Author> {
    @Query("MATCH (a:author) RETURN a")
    List<Author> getAuthorList();

    List<Author> findAllByANameEquals(String aName);

    List<Author> findAllByANameContains(String aName);

    List<Author> findAllByANameEqualsAndAUrlEquals(String aName, String aUrl);

    @Query("MATCH (a:author) WHERE a.aName =~ ('(?i).*'+{aName}+'.*') and a.aUrl =~ ('(?i).*'+{aUrl}+'.*') RETURN a")
    List<Author> findByNameAndUrl(@Param("aName") String aName, @Param("aUrl") String aUrl);

    @Query("MATCH w=(a:author)-[r:publish]->(p:paper) RETURN a as author, count(p) as paperCount order by count(p) desc limit {limit}")
    List<AuthorRankByPaperVO> findAuthorRankByPaper(@Param("limit") Integer limit);

    @Query("MATCH w=(a1:author)-[r1:publish]->(p:paper)<-[r2:publish]-(a2:author) " +
            "RETURN a1 as author1, a2 as author2, count(p) as paperCount order by count(p) desc limit {limit}")
    List<AuthorPartnersRankByPaperVO> findAuthorPartnersRankByPaper(@Param("limit") Integer limit);

    @Query("CREATE (a:author{aName:{aName}, aUrl:{aUrl}}) RETURN a")
    List<Author> addAuthor(@Param("aName") String aName, @Param("aUrl") String aUrl);

    @Query("MATCH (a:author) WHERE ID(a) = {id} SET a.aName = {aName}, a.aUrl = {aUrl} RETURN a")
    List<Author> editAuthor(@Param("id") Long id, @Param("aName") String aName, @Param("aUrl") String aUrl);

    @Query("MATCH (n) WHERE ID(n) IN {idList} OPTIONAL MATCH (n)-[r0]-() DELETE r0, n")
    void deleteAuthorIn(@Param("idList") List<Long> idList);
}