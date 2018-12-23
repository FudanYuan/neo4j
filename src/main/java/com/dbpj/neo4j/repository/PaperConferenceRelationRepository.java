package com.dbpj.neo4j.repository;

import com.dbpj.neo4j.relation.PaperConferenceRelation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Author: Jeremy
 * @Date: 2018/11/7 14:16
 */
public interface PaperConferenceRelationRepository extends GraphRepository<PaperConferenceRelation> {
    @Query("MATCH w=(p:paper)-[r:belong_to]->(c:conference) return w")
    List<PaperConferenceRelation> getAllRelationList();

    @Query("MATCH w=(p:paper)-[r:belong_to]->(c:conference) where ID(p) = {pId} return w")
    List<PaperConferenceRelation> findAllByPaperId(@Param("pId") Long pId);

    @Query("MATCH w=(p:paper)-[r:belong_to]->(c:conference) where p.pTitle =~ ('(?i).*'+{pTitle}+'.*') return w")
    List<PaperConferenceRelation> findAllByPaperTitle(@Param("pTitle") String pTitle);

    @Query("MATCH w=(p:paper)-[r:belong_to]->(c:conference) where ID(c) = {cId} return w")
    List<PaperConferenceRelation> findAllByConferenceId(@Param("cId") Long cId);

    @Query("MATCH w=(p:paper)-[r:belong_to]->(c:conference) where c.cName =~ ('(?i).*'+{cName}+'.*') return w")
    List<PaperConferenceRelation> findAllByConferenceName(@Param("cName") String cName);

    @Query("Match(p:paper)-[r:belong_to]->(c:conference) WHERE c.cName={cName} and p.pYear>={pYear1} and p.pYear<={pYear2} RETURN count(p)")
    Long findPaperCountByConferenceAndPYearBetween(@Param("cName") String cName, @Param("pYear1") Integer pYear1,  @Param("pYear2") Integer pYear2);

    @Query("MATCH (p:paper),(c:conference) where ID(p)={startId} and ID(c)={endId} " +
            "create w=(p)-[r:belong_to]->(c) return w")
    List<PaperConferenceRelation> addRelation(@Param("startId") Long startId, @Param("endId")  Long endId);

    @Query("MATCH (p:paper)-[r:belong_to]->(c:conference) WHERE ID(r) IN {idList} DELETE r")
    void deleteRelationIn(@Param("idList") List<Long> idList);
}
