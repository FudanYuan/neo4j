package com.dbpj.neo4j.controller;

import com.dbpj.neo4j.VO.ResultVO;
import com.dbpj.neo4j.enums.ResultEnum;
import com.dbpj.neo4j.node.Conference;
import com.dbpj.neo4j.service.ConferenceService;
import com.dbpj.neo4j.utils.ResultVOUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author: Jeremy
 * @Date: 2018/12/11 16:00
 */
@RestController
@RequestMapping("/neo4j/conference")
public class ConferenceController {

    @Autowired
    private ConferenceService conferenceService;

    // 插入会议
    @CrossOrigin
    @PostMapping("/insert")
    public ResultVO insertConferenceInfo(@RequestBody JSONObject jsonObject){
        String conferenceInfo = jsonObject.toString();
        if (conferenceInfo.equals("{}")) {
            return ResultVOUtil.error(ResultEnum.REQUEST_NULL);
        }
        String type = jsonObject.get("type").toString();
        if (!type.equals("3")){
            return ResultVOUtil.error(ResultEnum.TYPE_ERROR);
        }

        // 获取会议信息
        String cName = jsonObject.get("conference").toString();
        Conference conference = new Conference();
        conference.setCName(cName);

        // 记录执行时间
        long runtime = 0;
        long startTime = System.currentTimeMillis();   //获取开始时间

        conferenceService.save(conference);

        long endTime = System.currentTimeMillis(); //获取结束时间
        runtime += endTime-startTime;

        // 返回
        Map<String, Long> ret = new TreeMap<>();
        ret.put("time", runtime);

        return ResultVOUtil.success();
    }

    // 删除会议
    @CrossOrigin
    @PostMapping("/delete")
    public ResultVO deleteConferenceInfo(@RequestBody JSONObject jsonObject){
        String conferenceInfo = jsonObject.toString();
        if (conferenceInfo.equals("{}")) {
            return ResultVOUtil.error(ResultEnum.REQUEST_NULL);
        }
        String type = jsonObject.get("type").toString();
        if (!type.equals("3")){
            return ResultVOUtil.error(ResultEnum.TYPE_ERROR);
        }

        // 获取会议信息
        String cName = jsonObject.get("conference").toString();
        Conference conference = new Conference();
        conference.setCName(cName);

        // 记录执行时间
        long runtime = 0;
        long startTime = System.currentTimeMillis();   //获取开始时间
        List<Conference> conferenceList = conferenceService.findByCName(cName);
        conferenceService.deleteConferences(conferenceList);

        long endTime = System.currentTimeMillis(); //获取结束时间
        runtime += endTime-startTime;

        // 返回
        Map<String, Long> ret = new TreeMap<>();
        ret.put("time", runtime);

        return ResultVOUtil.success();
    }
}
