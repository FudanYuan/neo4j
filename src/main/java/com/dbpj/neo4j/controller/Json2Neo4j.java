package com.dbpj.neo4j.controller;

import com.dbpj.neo4j.vo.ResultVO;
import com.dbpj.neo4j.node.*;
import com.dbpj.neo4j.relation.AuthorDepartmentRelation;
import com.dbpj.neo4j.service.impl.*;
import com.dbpj.neo4j.utils.FileUtil;
import com.dbpj.neo4j.utils.ResultVOUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @Author: Jeremy
 * @Date: 2018/11/7 14:49
 */

@RestController
public class Json2Neo4j {

    @Autowired
    private AuthorServiceImpl authorService;

    @Autowired
    private PaperServiceImpl paperService;

    @Autowired
    private FieldServiceImpl fieldService;

    @Autowired
    private ConferenceServiceImpl conferenceService;

    @Autowired
    private DepartmentServiceImpl departmentService;

    @Autowired
    private AuthorDepartmentRelationServiceImpl authorDepartmentRelationService;

    @Autowired
    private AuthorPaperRelationServiceImpl authorPaperRelationService;

    @Autowired
    private PaperConferenceRelationServiceImpl paperConferenceRelationService;

    @Autowired
    private PaperFieldRelationServiceImpl paperFieldRelationService;

    private boolean test = true;

    private final String errorLogPath = "errorLog" + ( test ? "_test" : "" ) + ".txt";

    private final String successLogPath = "successLog" + ( test ? "_test" : "" ) + ".txt";

    private final String paperListPath = "paperList" + ( test ? "_test" : "" ) + ".txt";

    private final String runtimeLogPath = "runtimeLog" + ( test ? "_test" : "" ) + ".txt";

    // 获取跟目录
    @GetMapping("/importInfo")
    public ResultVO importInfo(){
        File filePath;
        try {
            filePath = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "static/mas_json/");
        } catch (IOException e){
            return ResultVOUtil.error(-1, "失败");
        }
        List<String> files= FileUtil.getAllFile(filePath.toString(), true);
        return insert2Neo4j(files, errorLogPath, successLogPath);
    }

    // 获取未添加列表
    @GetMapping("/paperList")
    public ResultVO getPaperList(){
        File filePath;
        try {
            filePath = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "static/mas_json/");
        } catch (IOException e){
            return ResultVOUtil.error(-1, "失败");
        }

        List<String> files= FileUtil.getAllFile(filePath.toString(), true);
        List<String> allPaperList = new ArrayList<>();
        for (String file : files){
            allPaperList.add(file);
        }
        System.out.println("all: " + allPaperList.size());

        // 读取已存文件
        List<String> okPaperList = FileUtil.readFileByLines(successLogPath);
        System.out.println("ok: " + okPaperList.size());

        allPaperList.removeAll(okPaperList);
        System.out.println("left: " + allPaperList.size());

        // 保存未添加列表
        FileUtil.deleteFile(paperListPath);
        for (String toPaper : allPaperList){
            FileUtil.writeLogFile(paperListPath, toPaper, true);
        }
        return ResultVOUtil.success("成功");
    }

    // 获取未添加列表
    public List<String> getToPaperList(){
        // 读取已存文件
        List<String> toPaperList = FileUtil.readFileByLines(paperListPath);
        List<String> okPaperList = FileUtil.readFileByLines(successLogPath);
        toPaperList.removeAll(okPaperList);
        System.out.println("left: " + toPaperList.size());

        // 保存未添加列表
        FileUtil.deleteFile(paperListPath);
        for (String toPaper : toPaperList){
            FileUtil.writeLogFile(paperListPath, toPaper, true);
        }
        return toPaperList;
    }

    // 将未添加列表添加至数据库
    @GetMapping("/addPaper")
    public ResultVO addPaper2Neo4j(){
        // 读取未添加文件
        List<String> toPaperList = getToPaperList();
        System.out.println("to: " + toPaperList.size());
        //return ResultVOUtil.success("成功");
        return insert2Neo4j(toPaperList, errorLogPath, successLogPath);
    }

    // 添加至数据库
    public ResultVO insert2Neo4j(List<String> files, String errorLogPath, String successLogPath){
        int count = 0;
        for (String file : files){
            String JsonContext = FileUtil.readFile(file);
            JsonObject jObject = new JsonParser().parse(JsonContext).getAsJsonObject();
            String error = "";
            boolean errorFlag = false;
            if (jObject == null){
                error = error.concat(file + "jObject == null\n");
                errorFlag=true;
            }
            if (!jObject.has("title")){
                error = error.concat(file + " title not in the key\n");
                errorFlag=true;
            }
            if (!jObject.has("abstract")){
                error = error.concat(file + " abstract not in the key\n");
                errorFlag=true;
            }
            if (!jObject.has("year")){
                error = error.concat(file + " year not in the key\n");
                errorFlag=true;
            }
            if (!jObject.has("citation")){
                error = error.concat(file + " pages not in the key\n");
                errorFlag=true;
            }
            if (!jObject.has("pages")){
                error = error.concat(file + " pages not in the key\n");
                errorFlag=true;
            }
            if (!jObject.has("conf")){
                error = error.concat(file + " conf not in the key\n");
                errorFlag=true;
            }
            if (!jObject.has("fields")){
                error = error.concat(file + " fields not in the key\n");
                errorFlag=true;
            }
            if (!jObject.has("authors")){
                error = error.concat(file + " authors not in the key\n");
                errorFlag=true;
            }
            if (!jObject.has("departments")){
                error = error.concat(file + " departments not in the key\n");
                errorFlag=true;
            }

            // 如果出现错误，则记录错误文件及原因
            if (errorFlag){
                FileUtil.writeLogFile(errorLogPath, error, true);
                continue;
            }

            /**
            /**
             * paper info
             */
            String pTitle = jObject.get("title").toString()
                    .replaceAll("\"", "")
                    .replaceAll("\\\\", "");
            String pAbstract = jObject.get("abstract").toString()
                    .replaceAll("\"", "")
                    .replaceAll("\\\\", "");
            Integer pYear = jObject.get("year").getAsInt();
            String citation = jObject.get("citation").toString();
            citation = Pattern.compile("[^0-9]").matcher(citation)
                    .replaceAll("")
                    .replaceAll("\"", "")
                    .replaceAll("\\\\", "");
            if (citation.equals("")){
                citation = "0";
            }
            Integer pCitation = Integer.valueOf(citation);
            Integer pPage = jObject.get("pages").getAsInt();

            Paper paper = new Paper();
            paper.setPTitle(pTitle);
            paper.setPAbstract(pAbstract);
            paper.setPYear(pYear);
            paper.setPCitation(pCitation);
            paper.setPPage(pPage);

            /**
             * conference info
             */
            String cName = jObject.get("conf").toString()
                    .replaceAll("\"", "")
                    .replaceAll("\\\\", "")
                    .toUpperCase();
            Conference conference = new Conference();
            conference.setCName(cName);

            /**
             * fields info
             */
            List<Field> fieldList = new ArrayList<>();
            JsonArray fields = jObject.get("fields").getAsJsonArray();
            for (JsonElement field : fields){
                String fName = field.toString()
                        .replaceAll("\"", "")
                        .replaceAll("\\\\", "");
                Field f = new Field();
                f.setFName(fName);
                fieldList.add(f);
            }

            /**
             * authors info
             */
            List<Author> authorList = new ArrayList<>();
            JsonArray authors = jObject.get("authors").getAsJsonArray();
            for (JsonElement author : authors){
                String authorInfo = author.toString()
                        .replaceAll("\"", "")
                        .replaceAll("\\\\", "");
                String[] info = authorInfo.split("\\.\\.\\.\\.\\.");
                String aName = info[0];
                String aUrl = info[1];

                Author a = new Author();
                a.setAName(aName);
                a.setAUrl(aUrl);
                authorList.add(a);
            }

            /**
             * departments info
             */
            List<Department> departmentList = new ArrayList<>();
            JsonArray departments = jObject.get("departments").getAsJsonArray();
            for (JsonElement department : departments){
                String dName = department.toString()
                        .replaceAll("\"", "")
                        .replaceAll("\\\\", "");
                Department d = new Department();
                d.setDName(dName);
                d.setDAddress("");
                departmentList.add(d);
            }

            long startTime = System.currentTimeMillis();   //获取开始时间
            // 插入论文信息
            Long pId;
            List<Paper> paperRes = paperService.findByTitle(pTitle);
            if (paperRes.size() == 0){
                List<Paper> saveRes = paperService.save(paper);
                pId = saveRes.get(0).getId();
            } else{
                pId = paperRes.get(0).getId();
            }

            // 插入作者信息
            List<Long> authorIndexList = new ArrayList<>();
            for (Author a : authorList){
                Long aId;
                List<Author> res = authorService.findAllByNameEqualsAndUrlEquals(a.getAName(), a.getAUrl());
                if (res.size() == 0){ // 数据库不存在则插入
                    List<Author> saveRes = authorService.save(a);
                    aId = saveRes.get(0).getId();
                } else {
                    aId = res.get(0).getId();
                }
                authorIndexList.add(aId);
            }

            // 插入单位信息
            List<Long> departmentIndexList = new ArrayList<>();
            for (Department d : departmentList){
                Long dId;
                List<Department> res = departmentService.findByDName(d.getDName());
                if (res.size() == 0){ // 数据库不存在则插入
                    List<Department> saveRes = departmentService.save(d);
                    dId = saveRes.get(0).getId();
                } else {
                    dId = res.get(0).getId();
                }
                if (!departmentIndexList.contains(dId)){
                    departmentIndexList.add(dId);
                }
            }

            // 插入会议信息
            Long cId;
            List<Conference> conferenceRes = conferenceService.findByCName(cName);
            if (conferenceRes.size() == 0){
                List<Conference> saveRes = conferenceService.save(conference);
                cId = saveRes.get(0).getId();
            } else{
                cId = conferenceRes.get(0).getId();
            }

            // 插入领域信息
            List<Long> fieldIndexList = new ArrayList<>();
            for (Field f : fieldList){
                Long fId;
                List<Field> fieldRes = fieldService.findByFName(f.getFName());
                if (fieldRes.size() == 0){ // 数据库不存在则插入
                    List<Field> saveRes = fieldService.save(f);
                    fId = saveRes.get(0).getId();
                } else {
                    fId = fieldRes.get(0).getId();
                }
                fieldIndexList.add(fId);
            }

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
            int i = 0;
            int j = 0;
            while (i <= authorIndexList.size()-1 && j <= departmentIndexList.size()-1){
                List<AuthorDepartmentRelation> ADRelationRes = authorDepartmentRelationService.findAllByAllParams(authorIndexList.get(i),
                        departmentIndexList.get(j), pYear, -1);
                if (ADRelationRes.size() == 0){
                    authorDepartmentRelationService.save(authorIndexList.get(i),
                            departmentIndexList.get(j), pYear, -1);
                }
                i++;
                j++;
            }

            long endTime=System.currentTimeMillis(); //获取结束时间
            long runtime = endTime-startTime;

            // 插入成功，记录日志
            FileUtil.writeLogFile(successLogPath, file, true);
            FileUtil.writeLogFile(runtimeLogPath, file + "," + runtime, true);
            count++;
        }
        return ResultVOUtil.success(count);
    }
}
