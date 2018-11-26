package com.dbpj.neo4j.service.impl;

import com.dbpj.neo4j.node.Conference;
import com.dbpj.neo4j.repository.ConferenceRepository;
import com.dbpj.neo4j.service.ConferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Jeremy
 * @Date: 2018/11/3 20:25
 */
@Service
public class ConferenceServiceImpl implements ConferenceService {
    @Autowired
    private ConferenceRepository conferenceRepository;

    @Override
    public Conference findById(Long id) {
        return conferenceRepository.findOne(id);
    }

    @Override
    public List<Conference> findAll() {
        return conferenceRepository.getConferenceList();
    }

    @Override
    public List<Conference> findByCName(String name){
        return conferenceRepository.findAllByCNameEquals(name);
    }

    @Override
    public List<Conference> findAllByCName(String fName) {
        return conferenceRepository.findAllByCNameContains(fName);
    }

    @Override
    public List<Conference> save(Conference conference) {
        return conferenceRepository.addConference(conference.getCName());
    }

    @Override
    public List<Conference> update(Conference conference) {
        return conferenceRepository.editConference(conference.getId(), conference.getCName());
    }

    @Override
    public void delete(Conference conference) {
        conferenceRepository.delete(conference.getId());
    }

    @Override
    public void deleteConferences(List<Conference> conferences) {
        List<Long> list = new ArrayList<>();
        for (Conference conference : conferences){
            list.add(conference.getId());
        }
        conferenceRepository.deleteConferenceIn(list);
    }
}
