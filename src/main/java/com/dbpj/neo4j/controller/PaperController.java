package com.dbpj.neo4j.controller;

import com.dbpj.neo4j.VO.ResultVO;
import com.dbpj.neo4j.enums.ResultEnum;
import com.dbpj.neo4j.node.*;
import com.dbpj.neo4j.relation.AuthorDepartmentRelation;
import com.dbpj.neo4j.service.*;
import com.dbpj.neo4j.service.impl.AuthorDepartmentRelationServiceImpl;
import com.dbpj.neo4j.service.impl.AuthorPaperRelationServiceImpl;
import com.dbpj.neo4j.service.impl.PaperConferenceRelationServiceImpl;
import com.dbpj.neo4j.service.impl.PaperFieldRelationServiceImpl;
import com.dbpj.neo4j.utils.ResultVOUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author: Jeremy
 * @Date: 2018/12/11 14:55
 */
@RestController
@RequestMapping("/neo4j/paper")
public class PaperController {
    @Autowired
    private PaperService paperService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private ConferenceService conferenceService;

    @Autowired
    private FieldService fieldService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private AuthorDepartmentRelationServiceImpl authorDepartmentRelationService;

    @Autowired
    private AuthorPaperRelationServiceImpl authorPaperRelationService;

    @Autowired
    private PaperConferenceRelationServiceImpl paperConferenceRelationService;

    @Autowired
    private PaperFieldRelationServiceImpl paperFieldRelationService;

    // 插入论文
    @CrossOrigin
    @PostMapping("/insert")
    public ResultVO insertPaperInfo(@RequestBody JSONObject jsonObject){
        String paperInfo = jsonObject.toString();
        if (paperInfo.equals("{}")) {
            return ResultVOUtil.error(ResultEnum.REQUEST_NULL);
        }
        String type = jsonObject.get("type").toString();
        if (!type.equals("2")){
            return ResultVOUtil.error(ResultEnum.TYPE_ERROR);
        }

        // 获取论文信息
        String pTitle = jsonObject.get("paperTitle").toString();
        String pYearStr = jsonObject.get("publishYear").toString();
        String pCitation = jsonObject.get("citationTime").toString();
        Integer pYear = Integer.valueOf(pYearStr);
        Paper paper = new Paper();
        paper.setPTitle(pTitle);
        paper.setPYear(pYear);
        paper.setPCitation(Integer.valueOf(pCitation));

        // 获取作者信息
        String aName = jsonObject.get("authorName").toString();
        String aUrl = jsonObject.get("authorURL").toString();
        List<Author> authorList = new ArrayList<>();
        Author author = new Author();
        author.setAName(aName);
        author.setAUrl(aUrl);
        authorList.add(author);

        // 获取会议信息
        String cName = jsonObject.get("conference").toString();

        List<Conference> conferenceList = new ArrayList<>();
        Conference conference = new Conference();
        conference.setCName(cName);
        conferenceList.add(conference);

        // 获取单位信息
        String dName = jsonObject.get("department").toString();
        List<Department> departmentList = new ArrayList<>();
        Department department = new Department();
        department.setDName(dName);
        departmentList.add(department);

        // 获取领域信息
        String fields = jsonObject.get("field").toString();
        String[] fName = fields.split(",");
        System.out.println(fName.toString());
        List<Field> fieldList = new ArrayList<>();
        for (int i=0; i<fName.length; i++){
            Field field = new Field();
            field.setFName(fName[i]);
            fieldList.add(field);
        }

        long startTime = System.currentTimeMillis();   //获取开始时间
        // 保存论文信息
        Long pId;
        List<Paper> paperRes = paperService.findByTitle(pTitle);
        if (paperRes.size() == 0){
            List<Paper> saveRes = paperService.save(paper);
            pId = saveRes.get(0).getId();
        } else{
            pId = paperRes.get(0).getId();
        }

        // 插入作者信息
        List<Long> authorIndexList = authorService.save(authorList);

        // 插入会议信息
        Long cId;
        List<Conference> conferenceRes = conferenceService.findByCName(cName);
        if (conferenceRes.size() == 0){
            List<Conference> saveRes = conferenceService.save(conference);
            cId = saveRes.get(0).getId();
        } else{
            cId = conferenceRes.get(0).getId();
        }

        // 插入单位信息
        List<Long> departmentIndexList = departmentService.save(departmentList);

        // 插入领域信息
        List<Long> fieldIndexList = fieldService.save(fieldList);

        // 插入论文-会议信息
        paperConferenceRelationService.save(pId, cId);

        // 插入论文-领域信息
        for (int i = 0; i<fieldIndexList.size(); i++){
            paperFieldRelationService.save(pId, fieldIndexList.get(i));
        }

        // 插入论文-作者信息
        for (int i=0; i<authorIndexList.size(); i++){
            authorPaperRelationService.save(authorIndexList.get(i), pId, i+1);
        }

        // 插入作者-单位信息
        authorDepartmentRelationService.save(authorIndexList, departmentIndexList, pYear);

        long endTime=System.currentTimeMillis(); //获取结束时间
        long runtime = endTime-startTime;

        Map<String, Long> ret = new TreeMap<>();
        ret.put("time", runtime);
        return ResultVOUtil.success(ret);
    }

    // 删除论文
    @CrossOrigin
    @PostMapping("/delete")
    public ResultVO deletePaperInfo(@RequestBody JSONObject jsonObject){
        String paperInfo = jsonObject.toString();
        if (paperInfo.equals("{}")) {
            return ResultVOUtil.error(ResultEnum.REQUEST_NULL);
        }
        String type = jsonObject.get("type").toString();
        if (!type.equals("2")){
            return ResultVOUtil.error(ResultEnum.TYPE_ERROR);
        }

        // 获取论文信息
        String pTitle = jsonObject.get("paperTitle").toString();
        String pYearStr = jsonObject.get("publishYear").toString();
        String pCitation = jsonObject.get("citationTime").toString();
        Integer pYear = Integer.valueOf(pYearStr);
        Paper paper = new Paper();
        paper.setPTitle(pTitle);
        paper.setPYear(pYear);
        paper.setPCitation(Integer.valueOf(pCitation));

        // 获取作者信息
        String aName = jsonObject.get("authorName").toString();
        String aUrl = jsonObject.get("authorURL").toString();
        List<Author> authorList = new ArrayList<>();
        Author author = new Author();
        author.setAName(aName);
        author.setAUrl(aUrl);
        authorList.add(author);

        // 获取会议信息
        String cName = jsonObject.get("conference").toString();

        List<Conference> conferenceList = new ArrayList<>();
        Conference conference = new Conference();
        conference.setCName(cName);
        conferenceList.add(conference);

        // 获取单位信息
        String dName = jsonObject.get("department").toString();
        List<Department> departmentList = new ArrayList<>();
        Department department = new Department();
        department.setDName(dName);
        departmentList.add(department);

        // 获取领域信息
        String fields = jsonObject.get("field").toString();
        String[] fName = fields.split(",");
        System.out.println(fName.toString());
        List<Field> fieldList = new ArrayList<>();
        for (int i=0; i<fName.length; i++){
            Field field = new Field();
            field.setFName(fName[i]);
            fieldList.add(field);
        }

        long startTime = System.currentTimeMillis();   //获取开始时间

        paperService.delete(paper);

        long endTime=System.currentTimeMillis(); //获取结束时间
        long runtime = endTime-startTime;

        Map<String, Long> ret = new TreeMap<>();
        ret.put("time", runtime);
        return ResultVOUtil.success(ret);
    }

}
