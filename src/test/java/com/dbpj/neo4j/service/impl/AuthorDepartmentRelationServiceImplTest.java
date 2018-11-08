package com.dbpj.neo4j.service.impl;

import com.dbpj.neo4j.node.Author;
import com.dbpj.neo4j.node.Department;
import com.dbpj.neo4j.relation.AuthorDepartmentRelation;
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
 * @Date: 2018/11/6 22:21
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
public class AuthorDepartmentRelationServiceImplTest {

    @Autowired
    private AuthorDepartmentRelationServiceImpl relationService;

    @Autowired
    private AuthorServiceImpl authorService;

    @Autowired
    private DepartmentServiceImpl departmentService;

    @Test
    public void findById() {
        AuthorDepartmentRelation relation = relationService.findById(Long.valueOf(92));
//        System.out.println(relation.getId());
//        System.out.println(relation.getAuthor().getAName());
        Assert.assertNotEquals(null, relation);
    }

    @Test
    public void findAll() {
        List<AuthorDepartmentRelation> relations = relationService.findAll();
        Assert.assertNotEquals(0, relations.size());
    }

    @Test
    public void findAllByAuthorName() {
        List<AuthorDepartmentRelation> relations = relationService.findAllByAuthorName("Jeremy");
        Assert.assertNotEquals(0, relations.size());
    }

    @Test
    public void findAllByDepartmentName() {
        List<AuthorDepartmentRelation> relations = relationService.findAllByDepartmentName("大学");
        Assert.assertNotEquals(0, relations.size());
    }

    @Test
    public void save() {
        AuthorDepartmentRelation relation = new AuthorDepartmentRelation();
        Author author = authorService.findById(Long.valueOf(0));
        Department department = departmentService.findById(Long.valueOf(21));
        relation.setAuthor(author);
        relation.setDepartment(department);
        relation.setSYear(2008);
        relation.setEYear(-1);
        List<AuthorDepartmentRelation> relations = relationService.save(relation);
        Assert.assertNotEquals(0, relations.size());
    }

    @Test
    public void update() {
        AuthorDepartmentRelation relation = relationService.findById(Long.valueOf(22));
        relation.setEYear(-1);
        relationService.update(relation);
    }

    @Test
    public void delete() {
        AuthorDepartmentRelation relation = relationService.findById(Long.valueOf(22));
        if (relation != null){
            relationService.delete(relation);
        }
    }

    @Test
    public void deleteRelations() {
        List<AuthorDepartmentRelation> relationList = new ArrayList<>();
        AuthorDepartmentRelation relation1 = new AuthorDepartmentRelation();
        relation1.setId(Long.valueOf(22));
        AuthorDepartmentRelation relation2 = new AuthorDepartmentRelation();
        relation2.setId(Long.valueOf(0));
        relationList.add(relation1);
        relationList.add(relation2);
        relationService.deleteRelations(relationList);
        List<AuthorDepartmentRelation> relations = relationService.findAll();
        Assert.assertEquals(0, relations.size());
    }
}