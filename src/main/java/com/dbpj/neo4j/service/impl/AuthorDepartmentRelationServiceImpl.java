package com.dbpj.neo4j.service.impl;

import com.dbpj.neo4j.node.Author;
import com.dbpj.neo4j.node.Conference;
import com.dbpj.neo4j.node.Department;
import com.dbpj.neo4j.relation.AuthorDepartmentRelation;
import com.dbpj.neo4j.repository.AuthorDepartmentRelationRepository;
import com.dbpj.neo4j.service.AuthorDepartmentRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Jeremy
 * @Date: 2018/11/6 22:15
 */
@Service
public class AuthorDepartmentRelationServiceImpl implements AuthorDepartmentRelationService {
    @Autowired
    private AuthorDepartmentRelationRepository relationRepository;

    @Override
    public AuthorDepartmentRelation findById(Long id) {
        return relationRepository.findOne(id);
    }

    @Override
    public List<AuthorDepartmentRelation> findAll() {
        return relationRepository.getAllRelationList();
    }

    @Override
    public List<AuthorDepartmentRelation> findAllByAuthorId(Long aId) {
        return relationRepository.findAllByAuthorId(aId);
    }

    @Override
    public List<AuthorDepartmentRelation> findAllByAuthorName(String aName) {
        return relationRepository.findAllByAuthor(aName);
    }

    @Override
    public List<AuthorDepartmentRelation> findAllByDepartmentId(Long dId) {
        return relationRepository.findAllByDepartmentId(dId);
    }

    @Override
    public List<AuthorDepartmentRelation> findAllByDepartmentName(String dName) {
        return relationRepository.findAllByDepartment(dName);
    }

    @Override
    public List<AuthorDepartmentRelation> findAllByAIdAndDId(Long aId, Long dId) {
        return relationRepository.findAllByAIdAndDId(aId, dId);
    }

    @Override
    public List<AuthorDepartmentRelation> findAllByAllParams(Long aId, Long dId, Integer sYear, Integer eYear) {
        return relationRepository.findAllByAllParams(aId, dId, sYear, eYear);
    }

    @Override
    public List<AuthorDepartmentRelation> save(AuthorDepartmentRelation relation) {
        return relationRepository.addRelation(relation.getAuthor().getId(), relation.getDepartment().getId(),
                relation.getSYear(), relation.getEYear());
    }

    @Override
    public List<AuthorDepartmentRelation> save(Long aId, Long dId, Integer sYear, Integer eYear) {
        return relationRepository.addRelation(aId, dId, sYear, eYear);
    }

    @Override
    public boolean save(List<Long> authorIndexList, List<Long> departmentIndexList, Integer pYear) {
        // 插入作者-单位信息
        int i = 0;
        int j = 0;
        while (i <= authorIndexList.size()-1 && j <= departmentIndexList.size()-1){
            List<AuthorDepartmentRelation> ADRelationRes = this.findAllByAllParams(authorIndexList.get(i),
                    departmentIndexList.get(j), pYear, -1);
            if (ADRelationRes.size() == 0){
                this.save(authorIndexList.get(i), departmentIndexList.get(j), pYear, -1);
            }
            i++;
            j++;
        }
        return true;
    }

    @Override
    public List<AuthorDepartmentRelation> update(AuthorDepartmentRelation relation) {
        return relationRepository.editRelation(relation.getId(), relation.getSYear(), relation.getEYear());
    }

    @Override
    public void delete(AuthorDepartmentRelation relation) {
        relationRepository.delete(relation.getId());
    }

    @Override
    public void deleteRelations(List<AuthorDepartmentRelation> relations) {
        List<Long> list = new ArrayList<>();
        for (AuthorDepartmentRelation relation : relations){
            list.add(relation.getId());
        }
        relationRepository.deleteRelationIn(list);
    }
}
