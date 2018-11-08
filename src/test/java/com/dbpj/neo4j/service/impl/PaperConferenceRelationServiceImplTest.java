package com.dbpj.neo4j.service.impl;

import com.dbpj.neo4j.node.Conference;
import com.dbpj.neo4j.node.Paper;
import com.dbpj.neo4j.relation.PaperConferenceRelation;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;


/**
 * @Author: Jeremy
 * @Date: 2018/11/7 14:28
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PaperConferenceRelationServiceImplTest {

    @Autowired
    private PaperConferenceRelationServiceImpl relationService;

    @Autowired
    private PaperServiceImpl paperService;

    @Autowired
    private ConferenceServiceImpl conferenceService;
    
    @Test
    public void findById() {
        PaperConferenceRelation relation = relationService.findById(Long.valueOf(1));
        Assert.assertNotEquals(null, relation);
    }

    @Test
    public void findAll() {
        List<PaperConferenceRelation> relations = relationService.findAll();
        Assert.assertNotEquals(0, relations.size());
    }

    @Test
    public void findAllByPaper() {
        Paper paper = paperService.findById(Long.valueOf(116));
        List<PaperConferenceRelation> relations = relationService.findAllByPaper(paper);
        Assert.assertNotEquals(0, relations.size());
    }

    @Test
    public void findAllByPaperId() {
        List<PaperConferenceRelation> relations = relationService.findAllByPaperId(Long.valueOf(116));
        Assert.assertNotEquals(0, relations.size());
    }

    @Test
    public void findAllByPaperTitle() {
        List<PaperConferenceRelation> relations = relationService.findAllByPaperTitle("oppo");
        Assert.assertNotEquals(0, relations.size());
    }

    @Test
    public void findAllByConference() {
        Conference conference = conferenceService.findById(Long.valueOf(22));
        List<PaperConferenceRelation> relations = relationService.findAllByConference(conference);
        Assert.assertNotEquals(0, relations.size());
    }

    @Test
    public void findAllByConferenceId() {
        List<PaperConferenceRelation> relations = relationService.findAllByConferenceId(Long.valueOf(22));
        Assert.assertNotEquals(0, relations.size());
    }

    @Test
    public void findAllByConferenceName() {
        List<PaperConferenceRelation> relations = relationService.findAllByConferenceName("IEEE");
        Assert.assertNotEquals(0, relations.size());
    }

    @Test
    public void save() {
        PaperConferenceRelation relation = new PaperConferenceRelation();
        Paper paper = paperService.findById(Long.valueOf(116));
        Conference conference = conferenceService.findById(Long.valueOf(96));
        relation.setPaper(paper);
        relation.setConference(conference);
        List<PaperConferenceRelation> relations = relationService.save(relation);
        Assert.assertNotEquals(0, relations.size());
    }

    @Test
    public void delete() {
        PaperConferenceRelation relation = relationService.findById(Long.valueOf(1));
        relationService.delete(relation);
    }

    @Test
    public void deleteRelations() {
        List<PaperConferenceRelation> relationList = new ArrayList<>();
        PaperConferenceRelation relation1 = new PaperConferenceRelation();
        relation1.setId(Long.valueOf(75));
        PaperConferenceRelation relation2 = new PaperConferenceRelation();
        relation2.setId(Long.valueOf(0));
        relationList.add(relation1);
        relationList.add(relation2);
        relationService.deleteRelations(relationList);
        List<PaperConferenceRelation> relations = relationService.findAll();
        Assert.assertEquals(0, relations.size());

    }
}