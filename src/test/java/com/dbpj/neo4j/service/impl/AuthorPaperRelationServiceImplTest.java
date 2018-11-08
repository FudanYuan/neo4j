package com.dbpj.neo4j.service.impl;

import com.dbpj.neo4j.node.Author;
import com.dbpj.neo4j.node.Paper;
import com.dbpj.neo4j.relation.AuthorPaperRelation;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @Author: Jeremy
 * @Date: 2018/11/7 09:40
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthorPaperRelationServiceImplTest {

    @Autowired
    private AuthorPaperRelationServiceImpl relationService;

    @Autowired
    private AuthorServiceImpl authorService;

    @Autowired
    private PaperServiceImpl paperService;
    
    @Test
    public void findById() {
        AuthorPaperRelation relation = relationService.findById(Long.valueOf(20));
        Assert.assertNotEquals(null, relation);
    }

    @Test
    public void findAll() {
        List<AuthorPaperRelation> relations = relationService.findAll();
        Assert.assertNotEquals(0, relations.size());
    }

    @Test
    public void findAllByAuthor() {
        Author author = authorService.findById(Long.valueOf(0));
        List<AuthorPaperRelation> relations = relationService.findAllByAuthor(author);
        Assert.assertNotEquals(0, relations.size());
    }

    @Test
    public void findAllByAuthorId() {
        List<AuthorPaperRelation> relations = relationService.findAllByAuthorId(Long.valueOf(0));
        Assert.assertNotEquals(0, relations.size());
    }

    @Test
    public void findAllByAuthorName() {
        List<AuthorPaperRelation> relations = relationService.findAllByAuthorName("Jeremy14");
        Assert.assertNotEquals(0, relations.size());
    }

    @Test
    public void findAllByPaper() {
        Paper paper = paperService.findById(Long.valueOf(116));
        List<AuthorPaperRelation> relations = relationService.findAllByPaper(paper);
        Assert.assertNotEquals(0, relations.size());
    }

    @Test
    public void findAllByPaperId() {
        List<AuthorPaperRelation> relations = relationService.findAllByPaperId(Long.valueOf(116));
        Assert.assertNotEquals(0, relations.size());
    }

    @Test
    public void findAllByPaperName() {
        List<AuthorPaperRelation> relations = relationService.findAllByPaperTitle("oppo");
        Assert.assertNotEquals(0, relations.size());
    }

    @Test
    public void save() {
        AuthorPaperRelation relation = new AuthorPaperRelation();
        Author author = authorService.findById(Long.valueOf(0));
        Paper paper = paperService.findById(Long.valueOf(116));
        relation.setAuthor(author);
        relation.setPaper(paper);
        relation.setAIndex(1);
        List<AuthorPaperRelation> relations = relationService.save(relation);
        Assert.assertNotEquals(0, relations.size());
    }

    @Test
    public void update() {
        AuthorPaperRelation relation = relationService.findById(Long.valueOf(20));
        relation.setAIndex(3);
        List<AuthorPaperRelation> relations = relationService.update(relation);
        Assert.assertNotEquals(0, relations.size());
    }

    @Test
    public void delete() {
        AuthorPaperRelation relation = relationService.findById(Long.valueOf(22));
        relationService.delete(relation);
    }

    @Test
    public void deleteRelations() {
        List<AuthorPaperRelation> relationList = new ArrayList<>();
        AuthorPaperRelation relation1 = new AuthorPaperRelation();
        relation1.setId(Long.valueOf(21));
        AuthorPaperRelation relation2 = new AuthorPaperRelation();
        relation2.setId(Long.valueOf(0));
        relationList.add(relation1);
        relationList.add(relation2);
        relationService.deleteRelations(relationList);
        List<AuthorPaperRelation> relations = relationService.findAll();
        Assert.assertEquals(1, relations.size());
    }
}