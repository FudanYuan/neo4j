package com.dbpj.neo4j.service;

import com.dbpj.neo4j.node.Field;
import com.dbpj.neo4j.node.Paper;
import com.dbpj.neo4j.relation.PaperFieldRelation;

import java.util.List;

/**
 * @Author: Jeremy
 * @Date: 2018/11/7 13:53
 */
public interface PaperFieldRelationService {
    /**
     * 根据id进行查询
     */
    PaperFieldRelation findById(Long id);

    /**
     * 返回所有联系
     */
    List<PaperFieldRelation> findAll();

    /**
     * 根据论文查询
     * @param paper
     * @return
     */
    List<PaperFieldRelation> findAllByPaper(Paper paper);

    /**
     * 根据paper id进行查询
     * @param id
     * @return
     */
    List<PaperFieldRelation> findAllByPaperId(Long id);

    /**
     * 根据论文题目进行查询
     */
    List<PaperFieldRelation> findAllByPaperTitle(String pName);

    /**
     * 根据领域进行查询
     * @param field
     * @return
     */
    List<PaperFieldRelation> findAllByField(Field field);

    /**
     * 根据领域id进行查询
     * @param id
     * @return
     */
    List<PaperFieldRelation> findAllByFieldId(Long id);

    /**
     * 根据领域名称进行查询
     */
    List<PaperFieldRelation> findAllByFieldName(String fName);

    /**
     * 保存
     */
    List<PaperFieldRelation> save(PaperFieldRelation relation);

    /**
     * 保存
     */
    List<PaperFieldRelation> save(Long pId, Long fId);

    /**
     * 删除节点
     */
    void delete(PaperFieldRelation relation);

    /**
     * 批量删除
     */
    void deleteRelations(List<PaperFieldRelation> relations);

}
