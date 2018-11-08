package com.dbpj.neo4j.repository;

import com.dbpj.neo4j.node.Field;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Author: Jeremy
 * @Date: 2018/11/3 19:40
 */
public interface FieldRepository extends GraphRepository<Field> {
    @Query("MATCH (f:field) RETURN f")
    List<Field> getFieldList();

    List<Field> findAllByFNameContains(String fName);

    @Query("CREATE (f:field{fName:{fName}}) RETURN f")
    List<Field> addField(@Param("fName") String fName);

    @Query("MATCH (f:field) WHERE ID(f) = {id} SET f.fName = {fName} RETURN f")
    List<Field> editField(@Param("id") Long id, @Param("fName") String fName);

    @Query("MATCH (n) WHERE ID(n) IN {idList} OPTIONAL MATCH (n)-[r0]-() DELETE r0, n")
    void deleteFieldIn(@Param("idList") List<Long> idList);
}
