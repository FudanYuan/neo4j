package com.dbpj.neo4j.controller;

import com.dbpj.neo4j.VO.Neo4jGraphVO;
import com.dbpj.neo4j.VO.ResultVO;
import com.dbpj.neo4j.enums.CategoryEnum;
import com.dbpj.neo4j.enums.ResultEnum;
import com.dbpj.neo4j.node.*;
import com.dbpj.neo4j.relation.AuthorPaperRelation;
import com.dbpj.neo4j.service.AuthorPaperRelationService;
import com.dbpj.neo4j.service.AuthorService;
import com.dbpj.neo4j.utils.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Author: Jeremy
 * @Date: 2018/12/11 11:59
 */
@RestController
@RequestMapping("/neo4j/author")
public class AuthorController {
    @Autowired
    private AuthorService authorService;

    @Autowired
    private AuthorPaperRelationService authorPaperRelationService;

    // 查询作者信息
    @CrossOrigin
    @PostMapping("/simple-query")
    public ResultVO getAuthorInfo(@RequestParam("aName") String aName){
        System.out.println("您正在查询作者——" + aName);
        List<Author> authors = authorService.findAllByName(aName);
        return ResultVOUtil.success(authors);
    }

    // 查询作者合作信息
    @CrossOrigin
    @PostMapping("/cooperateWith")
    public ResultVO getAuthorsCooperateWith(@RequestParam(value = "type") Integer type,
                                       @RequestParam(value = "author", required = false, defaultValue = "") String author,
                                       @RequestParam(value = "showTime", required = false, defaultValue = "1") Integer limit,
                                       @RequestParam(value = "queryTime", required = false, defaultValue = "1") Integer queryTimes){
        System.out.println("type: " + type);
        System.out.println("author: " + author);
        System.out.println("showTime: " + limit);
        System.out.println("queryTime: " + queryTimes);

        // 如果type不为4，则忽略请求
        if(type != 4){
            return ResultVOUtil.error(ResultEnum.TYPE_ERROR);
        }

        // 判断author是id，name还是url
        // 记录执行时间
        long runtime = 0;
        long startTime = System.currentTimeMillis();   //获取开始时间

        List<AuthorPaperRelation> authorPaperRelations = authorPaperRelationService.findAuthorsCooperateWith(author, author, limit);

        long endTime=System.currentTimeMillis(); //获取结束时间
        runtime += endTime-startTime;

        // 返回
        Map<String, Long> ret = new TreeMap<>();
        ret.put("time", runtime);

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
        neo4jGraphVO.setTime(runtime);
        System.out.println(neo4jGraphVO.toString());
        return ResultVOUtil.success(neo4jGraphVO);
    }

    // 插入作者
    @CrossOrigin
    @PostMapping("/insert")
    public ResultVO insertAuthorInfo(@RequestParam(value = "type") Integer type,
                                     @RequestParam(value = "authorName") String aName,
                                     @RequestParam(value = "authorURL") String aUrl){
        if (type != 1){
            return ResultVOUtil.error(ResultEnum.TYPE_ERROR);
        }

        // 保存作者信息
        Author author = new Author();
        author.setAName(aName);
        author.setAUrl(aUrl);

        // 记录执行时间
        long runtime = 0;
        long startTime = System.currentTimeMillis();   //获取开始时间

        authorService.save(author);

        long endTime=System.currentTimeMillis(); //获取结束时间
        runtime += endTime-startTime;

        // 返回
        Map<String, Long> ret = new TreeMap<>();
        ret.put("time", runtime);
        return ResultVOUtil.success(ret);
    }

    // 删除作者
    @CrossOrigin
    @PostMapping("/delete")
    public ResultVO deleteAuthorInfo(@RequestParam(value = "type") Integer type,
                                     @RequestParam(value = "authorName") String aName,
                                     @RequestParam(value = "authorURL") String aUrl){
        if (type != 1){
            return ResultVOUtil.error(ResultEnum.TYPE_ERROR);
        }

        // 作者信息
        Author author = new Author();
        author.setAName(aName);
        author.setAUrl(aUrl);

        // 记录执行时间
        long runtime = 0;
        long startTime = System.currentTimeMillis();   //获取开始时间
        List<Author> authors = authorService.findAllByNameAndUrl(aName, aUrl);
        authorService.deleteAuthors(authors);
        long endTime=System.currentTimeMillis(); //获取结束时间
        runtime += endTime-startTime;

        // 返回
        Map<String, Long> ret = new TreeMap<>();
        ret.put("time", runtime);
        return ResultVOUtil.success(ret);
    }
}
