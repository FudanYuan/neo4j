package com.dbpj.neo4j.service.impl;

import com.dbpj.neo4j.node.Field;
import com.dbpj.neo4j.repository.FieldRepository;
import com.dbpj.neo4j.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Jeremy
 * @Date: 2018/11/3 19:48
 */
@Service
public class FieldServiceImpl implements FieldService {

    @Autowired
    private FieldRepository fieldRepository;

    @Override
    public Field findById(Long id) {
        return fieldRepository.findOne(id);
    }

    @Override
    public List<Field> findAll() {
        return fieldRepository.getFieldList();
    }

    @Override
    public List<Field> findByFName(String fName) {
        return fieldRepository.findAllByFNameEquals(fName);
    }

    @Override
    public List<Field> findAllByFName(String fName) {
        return fieldRepository.findAllByFNameContains(fName);
    }

    @Override
    public List<Field> save(Field field) {
        return fieldRepository.addField(field.getFName());
    }

    @Override
    public List<Long> save(List<Field> fieldList) {
        // 插入领域信息
        List<Long> indexList = new ArrayList<>();
        for (Field f : fieldList){
            Long fId;
            List<Field> fieldRes = this.findByFName(f.getFName());
            if (fieldRes.size() == 0){ // 数据库不存在则插入
                List<Field> saveRes = this.save(f);
                fId = saveRes.get(0).getId();
            } else {
                fId = fieldRes.get(0).getId();
            }
            if(!indexList.contains(fId)){
                indexList.add(fId);
            }
        }
        return null;
    }

    @Override
    public List<Field> update(Field field) {
        return fieldRepository.editField(field.getId(), field.getFName());
    }

    @Override
    public void delete(Field field) {
        fieldRepository.delete(field);
    }

    @Override
    public void deleteFields(List<Field> fields) {
        List<Long> list = new ArrayList<>();
        for (Field field : fields){
            list.add(field.getId());
        }
        fieldRepository.deleteFieldIn(list);
    }
}
