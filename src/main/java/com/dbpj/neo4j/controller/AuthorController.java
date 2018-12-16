package com.dbpj.neo4j.controller;

import com.dbpj.neo4j.VO.Neo4jGraphVO;
import com.dbpj.neo4j.VO.ResultVO;
import com.dbpj.neo4j.enums.ResultEnum;
import com.dbpj.neo4j.node.*;
import com.dbpj.neo4j.relation.AuthorPaperRelation;
import com.dbpj.neo4j.service.AuthorPaperRelationService;
import com.dbpj.neo4j.service.AuthorService;
import com.dbpj.neo4j.utils.Neo4jGraphVOUtil;
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

    @CrossOrigin
    @PostMapping("/cooperationBetween")
    public ResultVO getAuthorsCooperationBetween(@RequestParam("authorA") String authorA,
                                                 @RequestParam("authorB") String authorB,
                                                 @RequestParam(value = "showTime", required = false, defaultValue = "10") Integer limit,
                                                 @RequestParam(value = "queryTime", required = false, defaultValue = "1") Integer queryTimes){

        System.out.println("authorA: " + authorA);
        System.out.println("authorB: " + authorB);
        System.out.println("showTime: " + limit);
        System.out.println("queryTime: " + queryTimes);

        if(authorA.length() == 0 || authorB.length() == 0){
            return ResultVOUtil.error(ResultEnum.ERROR);
        }
        Integer authorAid = -1;
        Integer authorBid = -1;
        try {
            authorAid = new Integer(authorA);
        }catch(Exception e){
            System.out.println("authorA不是id" );
        }
        try {
            authorBid = new Integer(authorB);
        }catch(Exception e){
            System.out.println("authorB不是id" );
        }
        // 记录执行时间
        long runtime = 0;
        //获取开始时间
        long startTime = System.currentTimeMillis();

        List<AuthorPaperRelation> authorPaperRelations;

        if (authorAid >= 0 && authorBid >= 0){
            authorPaperRelations = authorPaperRelationService.findAuthorsCooperateBetweenWithId(authorAid, authorBid, limit);
        }
        else if (authorAid < 0 && authorBid < 0){
            authorPaperRelations = authorPaperRelationService.findAuthorsCooperateBetweenWithUrl(authorA, authorB, limit);
        }
        else{
            return ResultVOUtil.error(ResultEnum.TYPE_ERROR);
        }
        //获取结束时间
        long endTime=System.currentTimeMillis();
        runtime += endTime-startTime;

        Neo4jGraphVO neo4jGraphVO = Neo4jGraphVOUtil.getNeo4jGraphVOByAuthorPaperRelations(authorPaperRelations);
        neo4jGraphVO.setTime(runtime);
        System.out.println(neo4jGraphVO.toString());
        return ResultVOUtil.success(neo4jGraphVO);
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
        //获取开始时间
        long startTime = System.currentTimeMillis();

        List<AuthorPaperRelation> authorPaperRelations = authorPaperRelationService.findAuthorsCooperateWith(author, author, limit);

        //获取结束时间
        long endTime=System.currentTimeMillis();
        runtime += endTime-startTime;

        // 返回
        Neo4jGraphVO neo4jGraphVO = Neo4jGraphVOUtil.getNeo4jGraphVOByAuthorPaperRelations(authorPaperRelations);
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
        if(authors.size() == 0){
            return ResultVOUtil.success(ResultEnum.AUTHOR_NOT_EXISTS);
        }
        authorService.deleteAuthors(authors);
        long endTime=System.currentTimeMillis(); //获取结束时间
        runtime += endTime-startTime;

        // 返回
        Map<String, Long> ret = new TreeMap<>();
        ret.put("time", runtime);
        return ResultVOUtil.success(ret);
    }
}
