package com.dbpj.neo4j.controller;

import com.dbpj.neo4j.VO.ResultVO;
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

    //获取跟目录
    @GetMapping("/importInfo")
    public ResultVO importInfo(){
        File filePath;
        try {
            filePath = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "static/mas_json/");
        } catch (IOException e){
            return ResultVOUtil.error(-1, "失败");
        }

        boolean test = false;
        List<String> files= FileUtil.getAllFile(filePath.toString(), true);
        String errorLogPath = "errorLog" + ( test ? "_test" : "" ) + ".txt";
        String successLogPath = "successLogPath" + ( test ? "_test" : "" ) + ".txt";
        int count = 0;
        for (String file : files){
            System.out.println("to deal with " + file);
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
                FileUtil.writeLogFile(errorLogPath, error);
                continue;
            }

            /**
             * paper info
             */
            String pTitle = jObject.get("title").toString().replaceAll("\"", "");
            String pAbstract = jObject.get("abstract").toString().replaceAll("\"", "");
            Integer pYear = jObject.get("year").getAsInt();
            String citation = jObject.get("citation").toString();
            citation = Pattern.compile("[^0-9]").matcher(citation).replaceAll("");
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
            String cName = jObject.get("conf").toString().replaceAll("\"", "").toUpperCase();
            Conference conference = new Conference();
            conference.setCName(cName);

            /**
             * fields info
             */
            List<Field> fieldList = new ArrayList<>();
            JsonArray fields = jObject.get("fields").getAsJsonArray();
            for (JsonElement field : fields){
                String fName = field.toString().replaceAll("\"", "");;
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
                String authorInfo = author.toString().replaceAll("\"", "");;
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
                String dName = department.toString().replaceAll("\"", "");;
                Department d = new Department();
                d.setDName(dName);
                d.setDAddress("");
                departmentList.add(d);
            }

            // 插入论文信息
            Long pId;
            List<Paper> paperRes = paperService.findAllByTitle(pTitle);
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
                List<Author> res = authorService.findAllByNameAndUrl(a.getAName(), a.getAUrl());
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
                List<Department> res = departmentService.findAllByDName(d.getDName());
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
            List<Conference> conferenceRes = conferenceService.findAllByCName(cName);
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
                List<Field> fieldRes = fieldService.findAllByFName(f.getFName());
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

            // 插入成功，记录日志
            FileUtil.writeLogFile(successLogPath, file);
            count++;
//            if (count++ == 10){
//                break;
//            }
        }
        return ResultVOUtil.success(count);
    }
}
