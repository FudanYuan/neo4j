package com.dbpj.neo4j.service;

import com.dbpj.neo4j.node.Department;

import java.util.List;

/**
 * @Author: Jeremy
 * @Date: 2018/11/3 18:44
 */
public interface DepartmentService {
    /**
     * 根据id查询
     */
    Department findById(Long id);

    /**
     * 返回所有
     * @return
     */
    List<Department> findAll();

    /**
     * 根据名称查询，全相等
     * @param dName
     * @return
     */
    List<Department> findByDName(String dName);

    /**
     * 根据名称查询
     * @param dName
     * @return
     */
    List<Department> findAllByDName(String dName);

    /**
     * 根据地址查询
     * @param dAddress
     * @return
     */
    List<Department> findAllByDAddress(String dAddress);

    /**
     * 添加
     * @return
     */
    List<Department> save(Department department);

    /**
     * 更新
     * @return
     */
    List<Department> update(Department department);

    /**
     * 删除单节点
     * @param department
     */
    void delete(Department department);

    /**
     * 批量删除
     * @param departments
     */
    void deleteDepartments(List<Department> departments);
}
