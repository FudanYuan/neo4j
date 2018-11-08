package com.dbpj.neo4j.service.impl;

import com.dbpj.neo4j.node.Conference;
import com.dbpj.neo4j.node.Paper;
import com.dbpj.neo4j.relation.PaperConferenceRelation;
import com.dbpj.neo4j.repository.PaperConferenceRelationRepository;
import com.dbpj.neo4j.service.PaperConferenceRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Jeremy
 * @Date: 2018/11/7 14:22
 */
@Service
public class PaperConferenceRelationServiceImpl implements PaperConferenceRelationService {

    @Autowired
    private PaperConferenceRelationRepository relationRepository;

    @Override
    public PaperConferenceRelation findById(Long id) {
        return relationRepository.findOne(id);
    }

    @Override
    public List<PaperConferenceRelation> findAll() {
        return relationRepository.getAllRelationList();
    }

    @Override
    public List<PaperConferenceRelation> findAllByPaper(Paper paper) {
        return relationRepository.findAllByPaperId(paper.getId());
    }

    @Override
    public List<PaperConferenceRelation> findAllByPaperId(Long id) {
        return relationRepository.findAllByPaperId(id);
    }

    @Override
    public List<PaperConferenceRelation> findAllByPaperTitle(String pTitle) {
        return relationRepository.findAllByPaperTitle(pTitle);
    }

    @Override
    public List<PaperConferenceRelation> findAllByConference(Conference conference) {
        return relationRepository.findAllByConferenceId(conference.getId());
    }

    @Override
    public List<PaperConferenceRelation> findAllByConferenceId(Long id) {
        return relationRepository.findAllByConferenceId(id);
    }

    @Override
    public List<PaperConferenceRelation> findAllByConferenceName(String cName) {
        return relationRepository.findAllByConferenceName(cName);
    }

    @Override
    public List<PaperConferenceRelation> save(PaperConferenceRelation relation) {
        return relationRepository.addRelation(relation.getPaper().getId(),
                relation.getConference().getId());
    }

    @Override
    public List<PaperConferenceRelation> save(Long pId, Long cId) {
        return relationRepository.addRelation(pId, cId);
    }

    @Override
    public void delete(PaperConferenceRelation relation) {
        relationRepository.delete(relation.getId());

    }

    @Override
    public void deleteRelations(List<PaperConferenceRelation> relations) {
        List<Long> list = new ArrayList<>();
        for (PaperConferenceRelation relation : relations){
            list.add(relation.getId());
        }
        relationRepository.deleteRelationIn(list);
    }
}
