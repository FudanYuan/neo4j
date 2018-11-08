package com.dbpj.neo4j.repository;

import com.dbpj.neo4j.relation.PaperFieldRelation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Author: Jeremy
 * @Date: 2018/11/7 13:47
 */
public interface PaperFieldRelationRepository extends GraphRepository<PaperFieldRelation> {
    @Query("MATCH w=(p:paper)-[r:involve]->(f:field) return w")
    List<PaperFieldRelation> getAllRelationList();

    @Query("MATCH w=(p:paper)-[r:involve]->(f:field) where ID(p) = {pId} return w")
    List<PaperFieldRelation> findAllByPaperId(@Param("pId") Long pId);

    @Query("MATCH w=(p:paper)-[r:involve]->(f:field) where p.pTitle =~ ('(?i).*'+{pTitle}+'.*') return w")
    List<PaperFieldRelation> findAllByPaperTitle(@Param("pTitle") String pTitle);

    @Query("MATCH w=(p:paper)-[r:involve]->(f:field) where ID(f) = {fId} return w")
    List<PaperFieldRelation> findAllByFieldId(@Param("fId") Long fId);

    @Query("MATCH w=(p:paper)-[r:involve]->(f:field) where f.fName =~ ('(?i).*'+{fName}+'.*') return w")
    List<PaperFieldRelation> findAllByFieldName(@Param("fName") String fName);

    @Query("MATCH (p:paper),(f:field) where ID(p)={startId} and ID(f)={endId} " +
            "create w=(p)-[r:involve]->(f) return w")
    List<PaperFieldRelation> addRelation(@Param("startId") Long startId, @Param("endId")  Long endId);

    @Query("MATCH (p:paper)-[r:involve]->(f:field) WHERE ID(r) IN {idList} DELETE r")
    void deleteRelationIn(@Param("idList") List<Long> idList);
}
