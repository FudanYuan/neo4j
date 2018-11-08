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
    public List<Author> findAllByName(String name) {
        return authorRepository.findByName(name);
    }

    @Override
    public List<Author> findAllByNameAndUrl(String name, String url) {
        return authorRepository.findByNameAndUrl(name, url);
    }

    @Override
    public List<Author> save(Author author) {
        return authorRepository.addAuthor(author.getAName(), author.getAUrl());
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
