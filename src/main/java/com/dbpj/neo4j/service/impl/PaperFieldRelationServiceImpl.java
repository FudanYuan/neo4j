package com.dbpj.neo4j.service.impl;

import com.dbpj.neo4j.node.Field;
import com.dbpj.neo4j.node.Paper;
import com.dbpj.neo4j.relation.PaperFieldRelation;
import com.dbpj.neo4j.repository.PaperFieldRelationRepository;
import com.dbpj.neo4j.service.PaperFieldRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Jeremy
 * @Date: 2018/11/7 13:56
 */
@Service
public class PaperFieldRelationServiceImpl implements PaperFieldRelationService {

    @Autowired
    PaperFieldRelationRepository relationRepository;

    @Override
    public PaperFieldRelation findById(Long id) {
        return relationRepository.findOne(id);
    }

    @Override
    public List<PaperFieldRelation> findAll() {
        return relationRepository.getAllRelationList();
    }

    @Override
    public List<PaperFieldRelation> findAllByPaper(Paper paper) {
        return relationRepository.findAllByPaperId(paper.getId());
    }

    @Override
    public List<PaperFieldRelation> findAllByPaperId(Long id) {
        return relationRepository.findAllByPaperId(id);
    }

    @Override
    public List<PaperFieldRelation> findAllByPaperTitle(String pName) {
        return relationRepository.findAllByPaperTitle(pName);
    }

    @Override
    public List<PaperFieldRelation> findAllByField(Field field) {
        return relationRepository.findAllByFieldId(field.getId());
    }

    @Override
    public List<PaperFieldRelation> findAllByFieldId(Long id) {
        return relationRepository.findAllByFieldId(id);
    }

    @Override
    public List<PaperFieldRelation> findAllByFieldName(String fName) {
        return relationRepository.findAllByFieldName(fName);
    }

    @Override
    public List<PaperFieldRelation> save(PaperFieldRelation relation) {
        return relationRepository.addRelation(relation.getPaper().getId(),
                relation.getField().getId());
    }

    @Override
    public List<PaperFieldRelation> save(Long pId, Long fId) {
        return relationRepository.addRelation(pId, fId);
    }

    @Override
    public void delete(PaperFieldRelation relation) {
        relationRepository.delete(relation.getId());
    }

    @Override
    public void deleteRelations(List<PaperFieldRelation> relations) {
        List<Long> list = new ArrayList<>();
        for (PaperFieldRelation relation : relations){
            list.add(relation.getId());
        }
        relationRepository.deleteRelationIn(list);
    }
}
