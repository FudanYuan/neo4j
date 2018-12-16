package com.dbpj.neo4j.controller;

import com.dbpj.neo4j.VO.Neo4jGraphVO;
import com.dbpj.neo4j.VO.ResultVO;
import com.dbpj.neo4j.enums.CategoryEnum;
import com.dbpj.neo4j.enums.ResultEnum;
import com.dbpj.neo4j.node.*;
import com.dbpj.neo4j.service.*;
import com.dbpj.neo4j.service.impl.AuthorDepartmentRelationServiceImpl;
import com.dbpj.neo4j.service.impl.AuthorPaperRelationServiceImpl;
import com.dbpj.neo4j.service.impl.PaperConferenceRelationServiceImpl;
import com.dbpj.neo4j.service.impl.PaperFieldRelationServiceImpl;
import com.dbpj.neo4j.utils.ResultVOUtil;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @Autowired
    private SessionFactory sessionFactory;

    // 查询作者信息
    @CrossOrigin
    @PostMapping("/simple-query")
    public ResultVO getSimpleQuery(@RequestParam(value = "type") Integer type,
                                   @RequestParam(value = "conference", required = false, defaultValue = "")String conference,
                                   @RequestParam(value = "author", required = false, defaultValue = "") String author,
                                   @RequestParam(value = "field", required = false, defaultValue = "") String field,
                                   @RequestParam(value = "publishYear", required = false, defaultValue = "") Integer publishYear,
                                   @RequestParam(value = "paperTitle", required = false, defaultValue = "") String paperTitle,
                                   @RequestParam(value = "showTime", required = false, defaultValue = "10") Integer limit,
                                   @RequestParam(value = "queryTime", required = false, defaultValue = "1") Integer queryTimes){
        System.out.println("您正在做链接查询");
        System.out.println("conference: " + conference);
        System.out.println("author: " + author);
        System.out.println("field: " + field);
        System.out.println("publishYear: " + publishYear);
        System.out.println("paperTitle: " + paperTitle);

        // 如果type不为4，则忽略请求
        if(type != 2 && type != 1){
            return ResultVOUtil.error(ResultEnum.TYPE_ERROR);
        }

        if(conference.length() == 0 && author.length() == 0 && field.length() == 0 && publishYear == null && paperTitle.length() == 0){
            return ResultVOUtil.error(ResultEnum.ERROR);
        }

        String publishYearString = "";
        String r = "p";
        boolean c_Flag = false;
        boolean a_Flag = false;
        boolean f_Flag = false;
        if(paperTitle.length() == 0){
            paperTitle = ".*";
        }
        if(publishYear != null){
            publishYearString = "AND p.pYear = " + publishYear;
        }
        if(conference.length() != 0){
            conference = " MATCH (c:conference) -- (p) WHERE c.cName = \"" + conference + "\"";
            r = r + ",c";
            c_Flag = true;
        }
        if(author.length() != 0){
            author = " MATCH (a:author) -- (p) WHERE a.aName = \"" + author + "\"";
            r = r + ",a";
            a_Flag = true;
        }
        if(field.length() != 0){
            field = " MATCH (f:field) -- (p) WHERE f.fName = \"" + field + "\"";
            r = r + ",f";
            f_Flag = true;
        }

        System.out.println("MATCH (p:paper) WHERE p.pTitle =~ ('(?i).*'+\"" + paperTitle  + "\"+'.*')" + publishYearString +
                conference +
                author +
                field +
                " RETURN " + r + " LIMIT " + limit);
        // 记录执行时间
        long runtime = 0;
        long startTime = System.currentTimeMillis();   //获取开始时间

//        List<Object> Objects = paperService.findAllByAll(conference, author, field, publishYearString, paperTitle, r, limit);
        Result result = sessionFactory.openSession().query("MATCH (p:paper) WHERE p.pTitle =~ ('(?i).*'+\"" + paperTitle  + "\"+'.*')" + publishYearString +
                conference +
                author +
                field +
                " RETURN " + r + " LIMIT " + limit,new TreeMap<>());
        long endTime=System.currentTimeMillis(); //获取结束时间
        runtime += endTime-startTime;

        // 返回
        Map<String, Long> ret = new TreeMap<>();
        ret.put("time", runtime);
        Iterable resultlist = result.queryResults();

        Neo4jGraphVO neo4jGraphVO = new Neo4jGraphVO("force");
        int index = 0;
        Map<Long, Integer> indexMap = new HashMap<>();
        List<TreeMap> nodes = new ArrayList<>();
        List<TreeMap> links = new ArrayList<>();
        String pStr = "p";
        if(pStr.equals(r)){
            for (Iterator iter = resultlist.iterator(); iter.hasNext();) {
                LinkedHashMap<String,Paper> str = (LinkedHashMap<String,Paper>)iter.next();
                TreeMap<String, Object> paper = new TreeMap<>();
                Paper p = str.get("p");
                paper.put("name", p.getPTitle());
                paper.put("value", 1);
                paper.put("category", CategoryEnum.PAPER.getCode());
                nodes.add(paper);

            }
        }
        else {
            for (Iterator iter = resultlist.iterator(); iter.hasNext(); ) {
                LinkedHashMap<String, Object> str = (LinkedHashMap<String, Object>) iter.next();
                Paper p = (Paper)str.get("p");
                Long pId = p.getId();
                if (!indexMap.containsKey(pId)) {
                    indexMap.put(pId, index++);
                    TreeMap<String, Object> node = new TreeMap<>();
                    node.put("name", p.getPTitle());
                    node.put("value", 1);
                    node.put("category", CategoryEnum.PAPER.getCode());
                    nodes.add(node);
                }
                int sourceIndex = indexMap.get(pId);
                if(a_Flag){
                    TreeMap<String, Object> node = new TreeMap<>();
                    TreeMap<String, Integer> link = new TreeMap<>();
                    Author a = (Author) str.get("a");
                    Long aId = a.getId();
                    if (!indexMap.containsKey(aId)){
                        indexMap.put(aId, index++);
                        node.put("name", a.getAName());
                        node.put("value", 1);
                        node.put("category", CategoryEnum.AUTHOR.getCode());
                        nodes.add(node);
                    }
                    int targetIndex = indexMap.get(aId);

                    link.put("source", sourceIndex);
                    link.put("target", targetIndex);

                    links.add(link);
                }
                if(c_Flag){
                    TreeMap<String, Object> node = new TreeMap<>();
                    TreeMap<String, Integer> link = new TreeMap<>();
                    Conference c = (Conference) str.get("c");
                    Long cId = c.getId();
                    if (!indexMap.containsKey(cId)){
                        indexMap.put(cId, index++);
                        node.put("name", c.getCName());
                        node.put("value", 1);
                        node.put("category", CategoryEnum.AUTHOR.getCode());
                        nodes.add(node);
                    }
                    int targetIndex = indexMap.get(cId);

                    link.put("source", sourceIndex);
                    link.put("target", targetIndex);

                    links.add(link);
                }
                if(f_Flag){
                    TreeMap<String, Object> node = new TreeMap<>();
                    TreeMap<String, Integer> link = new TreeMap<>();
                    Field f = (Field) str.get("f");
                    Long fId = f.getId();
                    if (!indexMap.containsKey(fId)){
                        indexMap.put(fId, index++);
                        node.put("name", f.getFName());
                        node.put("value", 1);
                        node.put("category", CategoryEnum.AUTHOR.getCode());
                        nodes.add(node);
                    }
                    int targetIndex = indexMap.get(fId);

                    link.put("source", sourceIndex);
                    link.put("target", targetIndex);

                    links.add(link);
                }


            }
        }
        neo4jGraphVO.setNodes(nodes);
        neo4jGraphVO.setLinks(links);
        neo4jGraphVO.setTime(runtime);
        System.out.println(neo4jGraphVO.toString());
        return ResultVOUtil.success(neo4jGraphVO);
    }

    // 插入论文
    @CrossOrigin
    @PostMapping("/insert")
    public ResultVO insertPaperInfo(@RequestParam(value = "type") Integer type,
                                    @RequestParam(value = "authorName") String aName,
                                    @RequestParam(value = "authorURL") String aUrl,
                                    @RequestParam(value = "paperTitle") String pTitle,
                                    @RequestParam(value = "conference") String cName,
                                    @RequestParam(value = "publishYear") Integer pYear,
                                    @RequestParam(value = "citationTime") Integer pCitation,
                                    @RequestParam(value = "department") String department,
                                    @RequestParam(value = "field") String field){
        if (type != 2){
            return ResultVOUtil.error(ResultEnum.TYPE_ERROR);
        }

        // 获取论文信息
        Paper paper = new Paper();
        paper.setPTitle(pTitle);
        paper.setPYear(pYear);
        paper.setPCitation(pCitation);

        // 获取作者信息
        List<Author> authorList = new ArrayList<>();
        Author author = new Author();
        author.setAName(aName);
        author.setAUrl(aUrl);
        authorList.add(author);

        // 获取会议信息
        List<Conference> conferenceList = new ArrayList<>();
        Conference conference = new Conference();
        conference.setCName(cName);
        conferenceList.add(conference);

        // 获取单位信息
        String[] dNames = department.split(",");
        List<Department> departmentList = new ArrayList<>();
        for (int i=0; i<dNames.length; i++){
            Department d = new Department();
            d.setDName(dNames[i]);
            departmentList.add(d);
        }

        // 获取领域信息
        String[] fNames = field.split(",");
        List<Field> fieldList = new ArrayList<>();
        for (int i=0; i<fNames.length; i++){
            Field f = new Field();
            f.setFName(fNames[i]);
            fieldList.add(f);
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
    public ResultVO deletePaperInfo(@RequestParam(value = "type") Integer type,
                                    @RequestParam(value = "authorName") String aName,
                                    @RequestParam(value = "authorURL") String aUrl,
                                    @RequestParam(value = "paperTitle") String pTitle,
                                    @RequestParam(value = "conference") String cName,
                                    @RequestParam(value = "publishYear") Integer pYear,
                                    @RequestParam(value = "citationTime") Integer pCitation,
                                    @RequestParam(value = "department") String department,
                                    @RequestParam(value = "field") String field){
        if (type != 2){
            return ResultVOUtil.error(ResultEnum.TYPE_ERROR);
        }

        // 获取论文信息
        Paper paper = new Paper();
        paper.setPTitle(pTitle);
        paper.setPYear(pYear);
        paper.setPCitation(pCitation);

        // 获取作者信息
        List<Author> authorList = new ArrayList<>();
        Author author = new Author();
        author.setAName(aName);
        author.setAUrl(aUrl);
        authorList.add(author);

        // 获取会议信息
        List<Conference> conferenceList = new ArrayList<>();
        Conference conference = new Conference();
        conference.setCName(cName);
        conferenceList.add(conference);

        // 获取单位信息
        String[] dNames = department.split(",");
        List<Department> departmentList = new ArrayList<>();
        for (int i=0; i<dNames.length; i++){
            Department d = new Department();
            d.setDName(dNames[i]);
            departmentList.add(d);
        }

        // 获取领域信息
        String[] fNames = field.split(",");
        List<Field> fieldList = new ArrayList<>();
        for (int i=0; i<fNames.length; i++){
            Field f = new Field();
            f.setFName(fNames[i]);
            fieldList.add(f);
        }

        long startTime = System.currentTimeMillis();   //获取开始时间

        List<Paper> paperList = paperService.findByTitle(pTitle);
        if(paperList.size() == 0){
            return ResultVOUtil.success(ResultEnum.PAPER_NOT_EXISTS);
        }
        paperService.deletePapers(paperList);

        long endTime=System.currentTimeMillis(); //获取结束时间
        long runtime = endTime-startTime;

        Map<String, Long> ret = new TreeMap<>();
        ret.put("time", runtime);
        return ResultVOUtil.success(ret);
    }

}
