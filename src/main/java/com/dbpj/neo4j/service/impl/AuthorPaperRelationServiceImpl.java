package com.dbpj.neo4j.service.impl;

import com.dbpj.neo4j.node.Author;
import com.dbpj.neo4j.node.Paper;
import com.dbpj.neo4j.relation.AuthorPaperRelation;
import com.dbpj.neo4j.repository.AuthorPaperRelationRepository;
import com.dbpj.neo4j.service.AuthorPaperRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Jeremy
 * @Date: 2018/11/7 09:25
 */
@Service
public class AuthorPaperRelationServiceImpl implements AuthorPaperRelationService {

    @Autowired
    private AuthorPaperRelationRepository relationRepository;

    @Override
    public AuthorPaperRelation findById(Long id) {
        return relationRepository.findOne(id);
    }

    @Override
    public List<AuthorPaperRelation> findAll() {
        return relationRepository.getAllRelationList();
    }

    @Override
    public List<AuthorPaperRelation> findAllByAuthor(Author author) {
        return relationRepository.findAllByAuthorId(author.getId());
    }

    @Override
    public List<AuthorPaperRelation> findAllByAuthorId(Long id) {
        return relationRepository.findAllByAuthorId(id);
    }

    @Override
    public List<AuthorPaperRelation> findAllByAuthorName(String aName) {
        return relationRepository.findAllByAuthorName(aName);
    }

    @Override
    public List<AuthorPaperRelation> findAllByPaper(Paper paper) {
        return relationRepository.findAllByPaperId(paper.getId());
    }

    @Override
    public List<AuthorPaperRelation> findAllByPaperId(Long id) {
        return relationRepository.findAllByPaperId(id);
    }

    @Override
    public List<AuthorPaperRelation> findAllByPaperTitle(String pName) {
        return relationRepository.findAllByPaperTitle(pName);
    }

    @Override
    public List<AuthorPaperRelation> findAuthorsCooperateWith(String name, String url, Integer k) {
        return relationRepository.findAuthorsCooperateWith(name, url, k);
    }

    @Override
    public List<AuthorPaperRelation> findAuthorsCooperateBetweenWithId(Integer authorA, Integer authorB, Integer k){
        return relationRepository.findAuthorsCooperateBetweenWithId(authorA, authorB, k);
    }

    @Override
    public List<AuthorPaperRelation> findAuthorsCooperateBetweenWithUrl(String authorA, String authorB, Integer k){
        return relationRepository.findAuthorsCooperateBetweenWithUrl(authorA, authorB, k);
    }

    @Override
    public List<AuthorPaperRelation> findAuthorsCooperateBetweenWithAuthorName(String authorA, String authorB, Integer k){
        return relationRepository.findAuthorsCooperateBetweenWithAuthorName(authorA, authorB, k);
    }

    @Override
    public List<AuthorPaperRelation> save(AuthorPaperRelation relation) {
        return relationRepository.addRelation(relation.getAuthor().getId(),
                relation.getPaper().getId(), relation.getAIndex());
    }

    @Override
    public List<AuthorPaperRelation> save(Long aId, Long pId, Integer aIndex) {
        return relationRepository.addRelation(aId, pId, aIndex);
    }

    @Override
    public List<AuthorPaperRelation> update(AuthorPaperRelation relation) {
        return relationRepository.editRelation(relation.getId(), relation.getAIndex());
    }

    @Override
    public void delete(AuthorPaperRelation relation) {
        relationRepository.delete(relation.getId());
    }

    @Override
    public void deleteRelations(List<AuthorPaperRelation> relations) {
        List<Long> list = new ArrayList<>();
        for (AuthorPaperRelation relation : relations){
            list.add(relation.getId());
        }
        relationRepository.deleteRelationIn(list);
    }
}
