package com.dbpj.neo4j.utils;

import com.dbpj.neo4j.VO.Neo4jGraphVO;
import com.dbpj.neo4j.enums.CategoryEnum;
import com.dbpj.neo4j.node.Author;
import com.dbpj.neo4j.node.Paper;
import com.dbpj.neo4j.relation.AuthorPaperRelation;

import java.util.*;

/**
 * @Author: Jeremy
 * @Date: 2018/12/16 11:36
 */
public class Neo4jGraphVOUtil {
    public static Neo4jGraphVO getNeo4jGraphVOByAuthorPaperRelations(List<AuthorPaperRelation> authorPaperRelations){
        Neo4jGraphVO neo4jGraphVO = new Neo4jGraphVO("force");
        List<TreeMap> nodes = new ArrayList<>();
        List<TreeMap> links = new ArrayList<>();
        int index = 0;
        Map<Long, Integer> indexMap = new HashMap<>();
        for (AuthorPaperRelation authorPaperRelation : authorPaperRelations){
            TreeMap<String, Object> node = new TreeMap<>();
            TreeMap<String, Object> paper = new TreeMap<>();
            TreeMap<String, Integer> link = new TreeMap<>();

            // 增加 author
            Author a = authorPaperRelation.getAuthor();
            Long aId = a.getId();
            if (!indexMap.containsKey(aId)){
                indexMap.put(aId, index++);
                node.put("name", authorPaperRelation.getAuthor().getAName());
                node.put("value", 1);
                node.put("category", CategoryEnum.AUTHOR.getCode());
                nodes.add(node);
            }
            int sourceIndex = indexMap.get(aId);

            // 增加 paper
            Paper p = authorPaperRelation.getPaper();
            Long pId = p.getId();
            if (!indexMap.containsKey(pId)){
                indexMap.put(pId, index++);
                paper.put("name", authorPaperRelation.getPaper().getPTitle());
                paper.put("value", 1);
                paper.put("category", CategoryEnum.PAPER.getCode());
                nodes.add(paper);
            }
            int targetIndex = indexMap.get(pId);

            link.put("source", sourceIndex);
            link.put("target", targetIndex);

            links.add(link);
        }

        neo4jGraphVO.setNodes(nodes);
        neo4jGraphVO.setLinks(links);
        return neo4jGraphVO;
    }
}
