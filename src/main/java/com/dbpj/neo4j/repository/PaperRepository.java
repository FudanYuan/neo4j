package com.dbpj.neo4j.repository;

import com.dbpj.neo4j.node.Paper;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Author: Jeremy
 * @Date: 2018/11/3 14:51
 */
public interface PaperRepository extends GraphRepository<Paper> {
    @Query("MATCH (p:paper) RETURN p")
    List<Paper> getPaperList();

    List<Paper> findAllByPYear(Integer year);

    List<Paper> findAllByPYearBefore(Integer year);

    List<Paper> findAllByPYearAfter(Integer year);

    List<Paper> findAllByPYearBetween(Integer year1, Integer year2);

    List<Paper> findAllByPTitleEquals(String title);

    List<Paper> findAllByPTitleContains(String title);

    @Query("MATCH (p:paper) WHERE p.pTitle = {paperTitle} " +
            "{publishYear}" +
            "{conference} " +
            "{author} " +
            "{field} " +
            " RETURN {r} LIMIT {k}")
    List<Object> findAllByAll(@Param("conference")String conference, @Param("author")String author,
                              @Param("field")String field, @Param("publishYear")String publishYear,
                              @Param("paperTitle")String paperTitle, @Param("r")String r,
                              @Param("k")Integer limit);

    @Query("MATCH (p:paper) RETURN p order by p.pCitation desc limit {limit}")
    List<Paper> findByPCitationCountLimit(@Param("limit") Integer limit);

    @Query("CREATE (p:paper{pTitle:{pTitle}, pAbstract: {pAbstract}, pPage:{pPage}, pCitation:{pCitation}, pYear:{pYear}}) RETURN p")
    List<Paper> addPaper(@Param("pTitle") String pTitle, @Param("pAbstract") String pAbstract,
                          @Param("pPage") Integer pPage, @Param("pCitation") Integer pCitation,
                          @Param("pYear") Integer pYear);

    @Query("MATCH (p:paper) WHERE ID(p) = {id} SET p.pTitle = {pTitle}, p.pAbstract = {pAbstract}, " +
            "p.pPage = {pPage}, p.pCitation = {pCitation}, p.pYear = {pYear} RETURN p")
    List<Paper> editPaper(@Param("id") Long id, @Param("pTitle") String pTitle, @Param("pAbstract") String pAbstract,
                         @Param("pPage") Integer pPage, @Param("pCitation") Integer pCitation,
                         @Param("pYear") Integer pYear);

    @Query("MATCH (n) WHERE ID(n) IN {idList} OPTIONAL MATCH (n)-[r0]-() DELETE r0, n")
    void deletePaperIn(@Param("idList") List<Long> idList);
}
