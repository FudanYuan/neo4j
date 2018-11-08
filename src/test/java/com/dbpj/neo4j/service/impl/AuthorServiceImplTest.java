package com.dbpj.neo4j.service.impl;

import com.dbpj.neo4j.node.Author;
import com.dbpj.neo4j.service.AuthorService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Jeremy
 * @Date: 2018/10/29 22:20
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthorServiceImplTest {

    @Autowired
    private AuthorService authorService;

    @Test
    public void findById() {
        Author author =  authorService.findById(Long.valueOf(0));
        Long id = author.getId();
        Assert.assertEquals(id, Long.valueOf(0));
    }

    @Test
    public void findAll() {
        List<Author> authors =  authorService.findAll();
        Assert.assertNotEquals(0, authors.size());
    }

    @Test
    public void findAllByName() {
        List<Author> authors =  authorService.findAllByName("WangPeng");
        Assert.assertEquals(1, authors.size());
    }

    @Test
    public void save() {
        Author author = new Author();
        author.setAName("Jeremy14");
        author.setAUrl("http://blog.hitaoxy.cn");
        List<Author> res = authorService.save(author);
        Assert.assertNotEquals(0, res.size());
    }

    @Test
    public void update() {
        Author author = authorService.findById(Long.valueOf(20));
        System.out.println(author.getId());
        author.setAName("Jeremy15");

        List<Author> authors = authorService.update(author);
        Assert.assertNotEquals(0, authors.size());
    }

    @Test
    public void delete() {
        Author author = authorService.findById(Long.valueOf(0));
        if (author != null){
            authorService.delete(author);
        }

        List<Author> authors = authorService.findAll();
        Assert.assertEquals(0, authors.size());
    }

    @Test
    public void deletePapers() {
        List<Author> authorList = new ArrayList<>();
        Author author1 = new Author();
        author1.setId(Long.valueOf(121));
        Author author2 = new Author();
        author2.setId(Long.valueOf(122));
        authorList.add(author1);
        authorList.add(author2);
        authorService.deleteAuthors(authorList);
        List<Author> authors = authorService.findAll();
        Assert.assertEquals(6, authors.size());
    }
}