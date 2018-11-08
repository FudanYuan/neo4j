package com.dbpj.neo4j.service.impl;

import com.dbpj.neo4j.node.Conference;
import com.dbpj.neo4j.service.ConferenceService;
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
 * @Date: 2018/11/3 20:28
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConferenceServiceImplTest {

    @Autowired
    private ConferenceService conferenceService;

    @Test
    public void findById() {
        Conference conference = conferenceService.findById(Long.valueOf(85));
        Assert.assertNotEquals(null, conference);
    }

    @Test
    public void findAll() {
        List<Conference> conferences = conferenceService.findAll();
        Assert.assertNotEquals(0, conferences.size());
    }

    @Test
    public void findAllByCName() {
        List<Conference> conferences = conferenceService.findAllByCName("AAAI");
        Assert.assertNotEquals(0, conferences.size());
    }

    @Test
    public void save() {
        Conference conference = new Conference();
        conference.setCName("SIGMOD");
        List<Conference> conferences = conferenceService.save(conference);
        Assert.assertNotEquals(0, conferences.size());
    }

    @Test
    public void update() {
        Conference conference = conferenceService.findById(Long.valueOf(143));
        conference.setCName("SIGMOD");
        List<Conference> conferences = conferenceService.update(conference);
        Assert.assertNotEquals(0, conferences.size());
    }

    @Test
    public void delete() {
        Conference conference = conferenceService.findById(Long.valueOf(161));
        if (conference != null){
            conferenceService.delete(conference);
        }
        Conference res = conferenceService.findById(Long.valueOf(161));
        Assert.assertEquals(null, res);
    }

    @Test
    public void deleteConferences() {
        List<Conference> conferenceList = new ArrayList<>();
        Conference conference1 = new Conference();
        conference1.setId(Long.valueOf(143));
        Conference conference2 = new Conference();
        conference2.setId(Long.valueOf(121));
        conferenceList.add(conference1);
        conferenceList.add(conference2);
        conferenceService.deleteConferences(conferenceList);
        List<Conference> conferences = conferenceService.findAll();
        Assert.assertEquals(0, conferences.size());
    }
}