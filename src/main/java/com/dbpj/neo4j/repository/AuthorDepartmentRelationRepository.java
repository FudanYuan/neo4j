package com.dbpj.neo4j.repository;

import com.dbpj.neo4j.relation.AuthorDepartmentRelation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Author: Jeremy
 * @Date: 2018/11/6 21:35
 */
public interface AuthorDepartmentRelationRepository extends GraphRepository<AuthorDepartmentRelation> {

    @Query("MATCH w=(a:author)-[r:work_in]->(d:department) return w")
    List<AuthorDepartmentRelation> getAllRelationList();

    @Query("MATCH w=(a:author)-[r:work_in]->(d:department) where ID(a) = {aId} return w")
    List<AuthorDepartmentRelation> findAllByAuthorId(@Param("aId") Long aId);

    @Query("MATCH w=(a:author)-[r:work_in]->(d:department) where a.aName =~ ('(?i).*'+{aName}+'.*') return w")
    List<AuthorDepartmentRelation> findAllByAuthor(@Param("aName") String aName);

    @Query("MATCH w=(a:author)-[r:work_in]->(d:department) where ID(d) = {dId} return w")
    List<AuthorDepartmentRelation> findAllByDepartmentId(@Param("dId") Long dId);

    @Query("MATCH w=(a:author)-[r:work_in]->(d:department) where d.dName =~ ('(?i).*'+{dName}+'.*') return w")
    List<AuthorDepartmentRelation> findAllByDepartment(@Param("dName") String dName);

    @Query("MATCH w=(a:author)-[r:work_in]->(d:department) where ID(a) = {aId} and ID(d) = {dId} return w")
    List<AuthorDepartmentRelation> findAllByAIdAndDId(@Param("aId") Long aId, @Param("dId") Long dId);

    @Query("MATCH w=(a:author)-[r:work_in]->(d:department) where ID(a) = {aId} and ID(d) = {dId} " +
            "and r.sYear = {sYear} and r.eYear = {eYear} return w")
    List<AuthorDepartmentRelation> findAllByAllParams(@Param("aId") Long aId, @Param("dId") Long dId,
                                                      @Param("sYear")  Integer sYear, @Param("eYear")  Integer eYear);

    @Query("MATCH (a:author),(d:department) where ID(a)={startId} and ID(d)={endId} " +
            "create w=(a)-[r:work_in{sYear: {sYear}, eYear:{eYear}}]->(d) return w")
    List<AuthorDepartmentRelation> addRelation(@Param("startId") Long startId, @Param("endId")  Long endId,
                                               @Param("sYear")  Integer sYear, @Param("eYear")  Integer eYear);

    @Query("MATCH (a:author)-[r:work_in]->(d:department) WHERE ID(r) = {id}" +
            " SET r.sYear = {sYear}, r.eYear = {eYear} RETURN r")
    List<AuthorDepartmentRelation> editRelation(@Param("id") Long id, @Param("sYear") Integer sYear,
                                                @Param("eYear") Integer eYear);

    @Query("MATCH (a:author)-[r:work_in]->(d:department) WHERE ID(r) IN {idList} DELETE r")
    void deleteRelationIn(@Param("idList") List<Long> idList);
}
