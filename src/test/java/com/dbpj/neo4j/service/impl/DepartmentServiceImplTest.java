package com.dbpj.neo4j.service.impl;

import com.dbpj.neo4j.node.Department;
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
 * @Date: 2018/11/3 19:05
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DepartmentServiceImplTest {

    @Autowired
    private DepartmentServiceImpl departmentService;

    @Test
    public void findById() {
        Department department = departmentService.findById(Long.valueOf(82));
        Assert.assertEquals("山东大学", department.getDName());
    }

    @Test
    public void findAllByDName() {
        List<Department> departments = departmentService.findAllByDName("大学");
        Assert.assertEquals(2, departments.size());
    }

    @Test
    public void findAllByDAddress() {
        List<Department> departments = departmentService.findAllByDAddress("上海");
        Assert.assertNotEquals(0, departments);
    }

    @Test
    public void save() {
        Department department = new Department();
        department.setDName("上海交通大学");
        department.setDAddress("上海市");

        List<Department> departments = departmentService.save(department);
        Assert.assertNotEquals(0, departments.size());
    }

    @Test
    public void update() {
        Department department = departmentService.findById(Long.valueOf(104));
        department.setDName("上海交通大学");

        List<Department> departments = departmentService.update(department);
        Assert.assertNotEquals(0, departments.size());
    }

    @Test
    public void delete() {
        Department department = departmentService.findById(Long.valueOf(181));
        if (department != null){
            departmentService.delete(department);
        }
        Department res = departmentService.findById(Long.valueOf(181));
        Assert.assertEquals(null, res);
    }

    @Test
    public void deleteDepartments() {
        List<Department> departmentList = new ArrayList<>();
        Department department1 = new Department();
        department1.setId(Long.valueOf(82));
        Department department2 = new Department();
        department2.setId(Long.valueOf(100));
        departmentList.add(department1);
        departmentList.add(department2);
        departmentService.deleteDepartments(departmentList);
        List<Department> departments = departmentService.findAll();
        Assert.assertEquals(1, departments.size());
    }
}