package com.dbpj.neo4j.service;

import com.dbpj.neo4j.node.Author;
import com.dbpj.neo4j.node.Paper;
import com.dbpj.neo4j.relation.AuthorPaperRelation;

import java.util.List;

/**
 * @Author: Jeremy
 * @Date: 2018/11/7 09:15
 */
public interface AuthorPaperRelationService {
    /**
     * 根据id进行查询
     */
    AuthorPaperRelation findById(Long id);

    /**
     * 返回所有联系
     */
    List<AuthorPaperRelation> findAll();

    /**
     * 根据作者进行查询
     * @param author
     * @return
     */
    List<AuthorPaperRelation> findAllByAuthor(Author author);

    /**
     * 根据作者id进行查询
     * @param id
     * @return
     */
    List<AuthorPaperRelation> findAllByAuthorId(Long id);

    /**
     * 根据作者姓名进行查询
     */
    List<AuthorPaperRelation> findAllByAuthorName(String aName);

    /**
     * 根据论文查询
     * @param paper
     * @return
     */
    List<AuthorPaperRelation> findAllByPaper(Paper paper);

    /**
     * 根据单位id进行查询
     * @param id
     * @return
     */
    List<AuthorPaperRelation> findAllByPaperId(Long id);

    /**
     * 根据论文题目进行查询
     */
    List<AuthorPaperRelation> findAllByPaperTitle(String pName);

    /**
     * 保存
     */
    List<AuthorPaperRelation> save(AuthorPaperRelation relation);

    List<AuthorPaperRelation> save(Long aId, Long pId, Integer aIndex);

    /**
     * 更改
     * @param relation
     * @return
     */
    List<AuthorPaperRelation> update(AuthorPaperRelation relation);

    /**
     * 删除节点
     */
    void delete(AuthorPaperRelation relation);

    /**
     * 批量删除
     */
    void deleteRelations(List<AuthorPaperRelation> relations);

}
