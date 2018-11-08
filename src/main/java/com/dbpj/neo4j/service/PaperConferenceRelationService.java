package com.dbpj.neo4j.service;

import com.dbpj.neo4j.node.Conference;
import com.dbpj.neo4j.node.Paper;
import com.dbpj.neo4j.relation.PaperConferenceRelation;

import java.util.List;

/**
 * @Author: Jeremy
 * @Date: 2018/11/7 14:19
 */
public interface PaperConferenceRelationService {
    /**
     * 根据id进行查询
     */
    PaperConferenceRelation findById(Long id);

    /**
     * 返回所有联系
     */
    List<PaperConferenceRelation> findAll();

    /**
     * 根据论文查询
     * @param paper
     * @return
     */
    List<PaperConferenceRelation> findAllByPaper(Paper paper);

    /**
     * 根据paper id进行查询
     * @param id
     * @return
     */
    List<PaperConferenceRelation> findAllByPaperId(Long id);

    /**
     * 根据论文题目进行查询
     */
    List<PaperConferenceRelation> findAllByPaperTitle(String pTitle);

    /**
     * 根据领域进行查询
     * @param conference
     * @return
     */
    List<PaperConferenceRelation> findAllByConference(Conference conference);

    /**
     * 根据会议id进行查询
     * @param id
     * @return
     */
    List<PaperConferenceRelation> findAllByConferenceId(Long id);

    /**
     * 根据领会议名称进行查询
     */
    List<PaperConferenceRelation> findAllByConferenceName(String cName);

    /**
     * 保存
     */
    List<PaperConferenceRelation> save(PaperConferenceRelation relation);

    /**
     * 保存
     */
    List<PaperConferenceRelation> save(Long pId, Long cId);

    /**
     * 删除节点
     */
    void delete(PaperConferenceRelation relation);

    /**
     * 批量删除
     */
    void deleteRelations(List<PaperConferenceRelation> relations);

}
