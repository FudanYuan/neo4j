package com.dbpj.neo4j.service;

import com.dbpj.neo4j.node.Paper;
import java.util.List;

/**
 * @Author: Jeremy
 * @Date: 2018/11/3 15:00
 */
public interface PaperService {
    /**
     * 根据id进行查询
     */
    Paper findById(Long id);

    /**
     * 返回所有论文
     */
    List<Paper> findAll();

    /**
     * 根据题目进行查询，全相等
     */
    List<Paper> findByTitle(String name);

    /**
     * 根据题目进行查询
     */
    List<Paper> findAllByTitle(String name);

    /**
     * 根据年份进行查询
     */
    List<Paper> findAllByYear(Integer year);

    /**
     * 根据<年份进行查询
     */
    List<Paper> findAllByYearBefore(Integer year);

    /**
     * 根据>年份进行查询
     */
    List<Paper> findAllByYearAfter(Integer year);

    /**
     * 根据<>年份进行查询
     */
    List<Paper> findAllByYearBetween(Integer year1, Integer year2);

    /**
     * 保存
     */
    List<Paper> save(Paper paper);

    /**
     * 更改
     * @param paper
     * @return
     */
    List<Paper> update(Paper paper);

    /**
     * 删除节点
     */
    void delete(Paper paper);

    /**
     * 批量删除
     */
    void deletePapers(List<Paper> papers);
}
