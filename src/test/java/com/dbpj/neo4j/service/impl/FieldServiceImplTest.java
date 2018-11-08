package com.dbpj.neo4j.service.impl;

import com.dbpj.neo4j.node.Field;
import com.dbpj.neo4j.service.FieldService;
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
 * @Date: 2018/11/3 19:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FieldServiceImplTest {

    @Autowired
    private FieldServiceImpl fieldService;

    @Test
    public void findById() {
        Field field = fieldService.findById(Long.valueOf(142));
        Assert.assertNotEquals(null, field);
    }

    @Test
    public void findAll() {
        List<Field> fields = fieldService.findAll();
        Assert.assertNotEquals(0, fields.size());
    }

    @Test
    public void findAllByFName() {
        List<Field> fields = fieldService.findAllByFName("机器学习");
        Assert.assertNotEquals(0, fields.size());
    }

    @Test
    public void save() {
        Field field = new Field();
        field.setFName("数据库");
        List<Field> fields = fieldService.save(field);
        Assert.assertNotEquals(0, fields.size());
    }

    @Test
    public void update() {
        Field field = fieldService.findById(Long.valueOf(84));
        field.setFName("计算机视觉");
        List<Field> fields = fieldService.update(field);
        Assert.assertNotEquals(0, fields.size());
    }

    @Test
    public void delete() {
        Field field = fieldService.findById(Long.valueOf(84));
        if (field != null){
            fieldService.delete(field);
        }
        Field res = fieldService.findById(Long.valueOf(84));
        Assert.assertEquals(null, res);
    }

    @Test
    public void deleteFields() {
        List<Field> fieldList = new ArrayList<>();
        Field field1 = new Field();
        field1.setId(Long.valueOf(142));
        Field field2 = new Field();
        field2.setId(Long.valueOf(170));
        fieldList.add(field1);
        fieldList.add(field2);
        fieldService.deleteFields(fieldList);
        List<Field> fields = fieldService.findAll();
        Assert.assertEquals(0, fields.size());
    }
}