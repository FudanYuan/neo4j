package com.dbpj.neo4j.service.impl;

import com.dbpj.neo4j.node.Field;
import com.dbpj.neo4j.node.Paper;
import com.dbpj.neo4j.relation.PaperFieldRelation;
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
 * @Date: 2018/11/7 14:01
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class PaperFieldRelationServiceImplTest {

    @Autowired
    private PaperFieldRelationServiceImpl relationService;

    @Autowired
    private PaperServiceImpl paperService;

    @Autowired
    private FieldServiceImpl fieldService;
    
    @Test
    public void findById() {
        PaperFieldRelation relation = relationService.findById(Long.valueOf(22));
        Assert.assertNotEquals(null, relation);
    }

    @Test
    public void findAll() {
        List<PaperFieldRelation> relations = relationService.findAll();
        Assert.assertNotEquals(0, relations.size());
    }

    @Test
    public void findAllByPaper() {
        Paper paper = paperService.findById(Long.valueOf(116));
        List<PaperFieldRelation> relations = relationService.findAllByPaper(paper);
        Assert.assertNotEquals(0, relations.size());
    }

    @Test
    public void findAllByPaperId() {
        List<PaperFieldRelation> relations = relationService.findAllByPaperId(Long.valueOf(116));
        Assert.assertNotEquals(0, relations.size());
    }

    @Test
    public void findAllByPaperTitle() {
        List<PaperFieldRelation> relations = relationService.findAllByPaperTitle("oppo");
        Assert.assertNotEquals(0, relations.size());
    }

    @Test
    public void findAllByField() {
        Field field = fieldService.findById(Long.valueOf(97));
        List<PaperFieldRelation> relations = relationService.findAllByField(field);
        Assert.assertNotEquals(0, relations.size());
    }

    @Test
    public void findAllByFieldId() {
        List<PaperFieldRelation> relations = relationService.findAllByFieldId(Long.valueOf(97));
        Assert.assertNotEquals(0, relations.size());
    }

    @Test
    public void findAllByFieldName() {
        List<PaperFieldRelation> relations = relationService.findAllByFieldName("数据库");
        Assert.assertNotEquals(0, relations.size());
    }

    @Test
    public void save() {
        PaperFieldRelation relation = new PaperFieldRelation();
        Paper paper = paperService.findById(Long.valueOf(116));
        Field field = fieldService.findById(Long.valueOf(97));
        relation.setPaper(paper);
        relation.setField(field);
        List<PaperFieldRelation> relations = relationService.save(relation);
        Assert.assertNotEquals(0, relations.size());

    }

    @Test
    public void delete() {
        PaperFieldRelation relation = relationService.findById(Long.valueOf(22));
        relationService.delete(relation);
    }

    @Test
    public void deleteRelations() {
        List<PaperFieldRelation> relationList = new ArrayList<>();
        PaperFieldRelation relation1 = new PaperFieldRelation();
        relation1.setId(Long.valueOf(23));
        PaperFieldRelation relation2 = new PaperFieldRelation();
        relation2.setId(Long.valueOf(0));
        relationList.add(relation1);
        relationList.add(relation2);
        relationService.deleteRelations(relationList);
        List<PaperFieldRelation> relations = relationService.findAll();
        Assert.assertEquals(0, relations.size());
    }
}