package com.dbpj.neo4j.service;

import com.dbpj.neo4j.node.Author;

import java.util.List;

/**
 * @Author: Jeremy
 * @Date: 2018/10/29 22:16
 */
public interface AuthorService {
    /**
     * 根据id进行查询
     */
    Author findById(Long id);

    /**
     * 返回所有作者
     */
    List<Author> findAll();

    /**
     * 根据姓名进行查询
     */
    List<Author> findAllByName(String name);

    /**
     * 根据姓名和url查询
     */
    List<Author> findAllByNameAndUrl(String name, String url);

    /**
     * 保存
     */
    List<Author> save(Author author);

    /**
     * 更改
     * @param author
     * @return
     */
    List<Author> update(Author author);

    /**
     * 删除节点
     */
    void delete(Author author);

    /**
     * 批量删除
     */
    void deleteAuthors(List<Author> authors);
}
