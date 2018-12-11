package com.dbpj.neo4j.repository;

import com.dbpj.neo4j.node.Conference;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Author: Jeremy
 * @Date: 2018/11/3 20:20
 */
public interface ConferenceRepository extends GraphRepository<Conference> {
    @Query("MATCH (c:conference) RETURN c")
    List<Conference> getConferenceList();

    List<Conference> findAllByCNameEquals(String cName);

    List<Conference> findAllByCNameContains(String cName);

    @Query("CREATE (c:conference{cName:{cName}}) RETURN c")
    List<Conference> addConference(@Param("cName") String cName);

    @Query("MATCH (c:conference) WHERE ID(c) = {id} SET c.cName = {cName} RETURN c")
    List<Conference> editConference(@Param("id") Long id, @Param("cName") String cName);
//
//    @Query("MATCH (n) WHERE ID(n) IN {idList} OPTIONAL MATCH (n)-[r0]-() DELETE r0, n")
//    void deleteByCName(@Param("cName") String cName);

    @Query("MATCH (n) WHERE ID(n) IN {idList} OPTIONAL MATCH (n)-[r0]-() DELETE r0, n")
    void deleteConferenceIn(@Param("idList") List<Long> idList);
}
