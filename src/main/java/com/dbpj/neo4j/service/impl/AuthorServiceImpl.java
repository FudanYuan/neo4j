package com.dbpj.neo4j.service.impl;

import com.dbpj.neo4j.node.Author;
import com.dbpj.neo4j.repository.AuthorRepository;
import com.dbpj.neo4j.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Jeremy
 * @Date: 2018/10/29 22:16
 */
@Service
public class AuthorServiceImpl implements AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public Author findById(Long id) {
        return authorRepository.findOne(id);
    }

    @Override
    public List<Author> findAll(){
        return authorRepository.getAuthorList();
    }

    @Override
    public List<Author> findByName(String name) {
        return authorRepository.findAllByANameEquals(name);
    }

    @Override
    public List<Author> findAllByName(String name) {
        return authorRepository.findAllByANameContains(name);
    }

    @Override
    public List<Author> findAllByNameAndUrl(String name, String url) {
        return authorRepository.findByNameAndUrl(name, url);
    }

    @Override
    public List<Author> findAllByNameEqualsAndUrlEquals(String name, String url) {
        return authorRepository.findAllByANameEqualsAndAUrlEquals(name, url);
    }

    @Override
    public List<Author> save(Author author) {
        return authorRepository.addAuthor(author.getAName(), author.getAUrl());
    }

    @Override
    public List<Long> save(List<Author> authorList) {
        List<Long> indexList = new ArrayList<>();
        for (Author a : authorList){
            Long aId;
            List<Author> res = this.findAllByNameEqualsAndUrlEquals(a.getAName(), a.getAUrl());
            if (res.size() == 0){ // 数据库不存在则插入
                List<Author> saveRes = this.save(a);
                aId = saveRes.get(0).getId();
            } else {
                aId = res.get(0).getId();
            }
            if(!indexList.contains(aId)){
                indexList.add(aId);
            }
        }
        return indexList;
    }

    @Override
    public List<Author> update(Author author) {
        return authorRepository.editAuthor(author.getId(), author.getAName(), author.getAUrl());
    }

    @Override
    public void delete(Author author) {
        authorRepository.delete(author.getId());
    }

    @Override
    public void deleteAuthors(List<Author> authors) {
        List<Long> list = new ArrayList<>();
        for (Author author : authors){
            list.add(author.getId());
        }
        authorRepository.deleteAuthorIn(list);
    }

}
