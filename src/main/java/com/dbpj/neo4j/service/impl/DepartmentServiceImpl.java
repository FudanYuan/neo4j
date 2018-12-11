package com.dbpj.neo4j.service.impl;

import com.dbpj.neo4j.node.Department;
import com.dbpj.neo4j.repository.DepartmentRepository;
import com.dbpj.neo4j.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Jeremy
 * @Date: 2018/11/3 18:49
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public Department findById(Long id) {
        return departmentRepository.findOne(id);
    }

    @Override
    public List<Department> findAll(){
        return departmentRepository.getDepartmentList();
    }

    @Override
    public List<Department> findByDName(String dName) {
        return departmentRepository.findAllByDNameEquals(dName);
    }

    @Override
    public List<Department> findAllByDName(String dName) {
        return departmentRepository.findAllByDNameContains(dName);
    }

    @Override
    public List<Department> findAllByDAddress(String dAddress) {
        return departmentRepository.findAllByDAddressContains(dAddress);
    }

    @Override
    public List<Department> save(Department department) {
        return departmentRepository.addDepartment(department.getDName(), department.getDAddress());
    }

    @Override
    public List<Long> save(List<Department> departmentList) {
        // 插入单位信息
        List<Long> indexList = new ArrayList<>();
        for (Department d : departmentList){
            Long dId;
            List<Department> res = this.findByDName(d.getDName());
            if (res.size() == 0){ // 数据库不存在则插入
                List<Department> saveRes = this.save(d);
                dId = saveRes.get(0).getId();
            } else {
                dId = res.get(0).getId();
            }
            if (!indexList.contains(dId)){
                indexList.add(dId);
            }
        }
        return indexList;
    }

    @Override
    public List<Department> update(Department department) {
        return departmentRepository.editDepartment(department.getId(), department.getDName(), department.getDAddress());
    }

    @Override
    public void delete(Department department) {
        departmentRepository.delete(department.getId());
    }

    @Override
    public void deleteDepartments(List<Department> departments) {
        List<Long> list = new ArrayList<>();
        for (Department department : departments){
            list.add(department.getId());
        }
        departmentRepository.deleteDepartmentIn(list);
    }
}
