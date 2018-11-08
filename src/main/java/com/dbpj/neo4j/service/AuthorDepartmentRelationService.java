package com.dbpj.neo4j.service;

import com.dbpj.neo4j.relation.AuthorDepartmentRelation;

import java.util.List;

/**
 * @AuthorDepartmentRelation: Jeremy
 * @Date: 2018/11/6 22:12
 */
public interface AuthorDepartmentRelationService {
    /**
     * 根据id进行查询
     */
    AuthorDepartmentRelation findById(Long id);

    /**
     * 返回所有联系
     */
    List<AuthorDepartmentRelation> findAll();

    /**
     * 根据作者id进行查询
     */
    List<AuthorDepartmentRelation> findAllByAuthorId(Long aId);

    /**
     * 根据作者姓名进行查询
     */
    List<AuthorDepartmentRelation> findAllByAuthorName(String aName);

    /**
     * 根据单位id进行查询
     */
    List<AuthorDepartmentRelation> findAllByDepartmentId(Long dId);

    /**
     * 根据单位名称进行查询
     */
    List<AuthorDepartmentRelation> findAllByDepartmentName(String dName);

    /**
     * 根据作者id\单位id进行查询
     */
    List<AuthorDepartmentRelation> findAllByAIdAndDId(Long aId, Long dId);

    /**
     * 根据所有参数进行查询
     */
    List<AuthorDepartmentRelation> findAllByAllParams(Long aId, Long dId, Integer sYear, Integer eYear);

    /**
     * 保存
     */
    List<AuthorDepartmentRelation> save(AuthorDepartmentRelation relation);

    /**
     * 保存
     */
    List<AuthorDepartmentRelation> save(Long aId, Long dId, Integer sYear, Integer eYear);

    /**
     * 更改
     * @param relation
     * @return
     */
    List<AuthorDepartmentRelation> update(AuthorDepartmentRelation relation);

    /**
     * 删除节点
     */
    void delete(AuthorDepartmentRelation relation);

    /**
     * 批量删除
     */
    void deleteRelations(List<AuthorDepartmentRelation> relations);
}
