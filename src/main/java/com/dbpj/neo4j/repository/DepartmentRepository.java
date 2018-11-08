package com.dbpj.neo4j.repository;

import com.dbpj.neo4j.node.Department;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Department: Jeremy
 * @Date: 2018/11/3 18:42
 */
public interface DepartmentRepository extends GraphRepository<Department> {
    @Query("MATCH (d:department) RETURN d")
    List<Department> getDepartmentList();

    List<Department> findAllByDNameContains(String dName);

    List<Department> findAllByDAddressContains(String aAddress);

    @Query("CREATE (d:department{dName:{dName}, dAddress:{dAddress}}) RETURN d")
    List<Department> addDepartment(@Param("dName") String dName, @Param("dAddress") String dAddress);

    @Query("MATCH (d:department) WHERE ID(d) = {id} SET d.dName = {dName}, d.dAddress = {dAddress} RETURN d")
    List<Department> editDepartment(@Param("id") Long id, @Param("dName") String dName, @Param("dAddress") String dAddress);

    @Query("MATCH (n) WHERE ID(n) IN {idList} OPTIONAL MATCH (n)-[r0]-() DELETE r0, n")
    void deleteDepartmentIn(@Param("idList") List<Long> idList);
}
