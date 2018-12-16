package com.dbpj.neo4j.repository;

import com.dbpj.neo4j.relation.AuthorPaperRelation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Author: Jeremy
 * @Date: 2018/11/7 09:09
 */
public interface AuthorPaperRelationRepository extends GraphRepository<AuthorPaperRelation> {

    @Query("MATCH w=(a:author)-[r:publish]->(p:paper) return w")
    List<AuthorPaperRelation> getAllRelationList();

    @Query("MATCH w=(a:author)-[r:publish]->(p:paper) where ID(a) = {aId} return w")
    List<AuthorPaperRelation> findAllByAuthorId(@Param("aId") Long aId);

    @Query("MATCH w=(a:author)-[r:publish]->(p:paper) where a.aName =~ ('(?i).*'+{aName}+'.*') return w")
    List<AuthorPaperRelation> findAllByAuthorName(@Param("aName") String aName);

    @Query("MATCH w=(a:author)-[r:publish]->(p:paper) where ID(p) = {pId} return w")
    List<AuthorPaperRelation> findAllByPaperId(@Param("pId") Long pId);

    @Query("MATCH w=(a:author)-[r:publish]->(p:paper) where p.pTitle =~ ('(?i).*'+{pTitle}+'.*') return w")
    List<AuthorPaperRelation> findAllByPaperTitle(@Param("pTitle") String pTitle);

    @Query("MATCH w=(a1:author)-[r1]->(p:paper)<-[r2]-(a2:author) " +
            "WHERE a1.aName =~ ('(?i).*'+{aName}+'.*') " +
            "OR a1.aUrl =~ ('(?i).*'+{aUrl}+'.*') " +
            "RETURN w, count(*) order by count(*) desc LIMIT {k}")
    List<AuthorPaperRelation> findAuthorsCooperateWith(@Param("aName") String aName, @Param("aUrl") String aUrl, @Param("k") Integer k);

    @Query("MATCH w=(a1:author)-[r1]->(p:paper)<-[r2]-(a2:author) " +
            "WHERE ID(a1) = {authorA} " +
            "AND ID(a2) = {authorB} " +
            "RETURN w LIMIT {k}")
    List<AuthorPaperRelation> findAuthorsCooperateBetweenWithId(@Param("authorA")Integer authorA, @Param("authorB")Integer authorB, @Param("k")Integer k);

    @Query("MATCH w=(a1:author)-[r1]->(p:paper)<-[r2]-(a2:author) " +
            "WHERE a1.aUrl =~ ('(?i).*'+{authorA}+'.*') " +
            "AND a2.aUrl =~ ('(?i).*'+{authorB}+'.*') " +
            "RETURN w LIMIT {k}")
    List<AuthorPaperRelation> findAuthorsCooperateBetweenWithUrl(@Param("authorA")String authorA, @Param("authorB")String authorB, @Param("k")Integer k);

    @Query("MATCH w=(a1:author)-[r1]->(p:paper)<-[r2]-(a2:author) " +
            "WHERE a1.aName =~ ('(?i).*'+{authorA}+'.*') " +
            "AND a2.aName =~ ('(?i).*'+{authorB}+'.*') " +
            "RETURN w LIMIT {k}")
    List<AuthorPaperRelation> findAuthorsCooperateBetweenWithAuthorName(@Param("authorA")String authorA, @Param("authorB")String authorB, @Param("k")Integer k);

    @Query("MATCH (a:author),(p:paper) where ID(a)={startId} and ID(p)={endId} " +
            "create w=(a)-[r:publish{aIndex:{aIndex}}]->(p) return w")
    List<AuthorPaperRelation> addRelation(@Param("startId") Long startId, @Param("endId")  Long endId,
                                          @Param("aIndex")  Integer aIndex);

    @Query("MATCH (a:author)-[r:publish]->(p:paper) WHERE ID(r) = {id}" +
            " SET r.aIndex = {aIndex} RETURN r")
    List<AuthorPaperRelation> editRelation(@Param("id") Long id, @Param("aIndex") Integer aIndex);

    @Query("MATCH (a:author)-[r:publish]->(p:paper) WHERE ID(r) IN {idList} DELETE r")
    void deleteRelationIn(@Param("idList") List<Long> idList);
}
