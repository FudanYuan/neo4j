package com.dbpj.neo4j.service;

import com.dbpj.neo4j.node.Field;

import java.util.List;

/**
 * @Author: Jeremy
 * @Date: 2018/11/3 19:46
 */
public interface FieldService {
    /**
     * 根据id查询
     */
    Field findById(Long id);

    /**
     * 返回所有
     * @return
     */
    List<Field> findAll();

    /**
     * 根据名称查询
     * @param fName
     * @return
     */
    List<Field> findByFName(String fName);

    /**
     * 根据名称查询
     * @param fName
     * @return
     */
    List<Field> findAllByFName(String fName);

    /**
     * 添加
     * @return
     */
    List<Field> save(Field field);

    /**
     * 更新
     * @return
     */
    List<Field> update(Field field);

    /**
     * 删除单节点
     * @param field
     */
    void delete(Field field);

    /**
     * 批量删除
     * @param fields
     */
    void deleteFields(List<Field> fields);
}
