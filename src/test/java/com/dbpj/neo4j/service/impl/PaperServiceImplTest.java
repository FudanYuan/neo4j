package com.dbpj.neo4j.service.impl;

import com.dbpj.neo4j.node.Paper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;


/**
 * @Author: Jeremy
 * @Date: 2018/11/3 16:23
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PaperServiceImplTest {

    @Autowired
    private PaperServiceImpl paperService;

    @Test
    public void findById() {
        Paper paper = paperService.findById(Long.valueOf(82));
        Assert.assertEquals(Integer.valueOf(100), paper.getPCitation());
    }

    @Test
    public void findAll() {
        List<Paper> papers = paperService.findAll();
        Assert.assertEquals(6, papers.size());
    }

    @Test
    public void findAllByTitle() {
        List<Paper> papers = paperService.findAllByTitle("时间");
        Assert.assertEquals(3, papers.size());
    }

    @Test
    public void findAllByYear() {
        List<Paper> papers = paperService.findAllByYear(2018);
        Assert.assertEquals(4, papers.size());
    }

    @Test
    public void findAllByYearBefore() {
        List<Paper> papers = paperService.findAllByYearBefore(2018);
        Assert.assertEquals(0, papers.size());
    }

    @Test
    public void findAllByYearAfter() {
        List<Paper> papers = paperService.findAllByYearAfter(2018);
        Assert.assertEquals(0, papers.size());
    }

    @Test
    public void findAllByYearBetween() {
        List<Paper> papers = paperService.findAllByYearBetween(2017, 2020);
        Assert.assertEquals(4, papers.size());
    }

    @Test
    public void save() {
        Paper paper = new Paper();
        paper.setPTitle("oppo");
        paper.setPAbstract("oppo");
        paper.setPPage(10);
        paper.setPCitation(100);
        paper.setPYear(2014);
        List<Paper> papers = paperService.save(paper);
        Assert.assertNotEquals(0, papers.size());
    }

    @Test
    public void update() {
        Paper paper = paperService.findAllByTitle("时间序列555").get(0);
        System.out.println(paper.getId());
        paper.setPTitle("时间序列667");

        List<Paper> papers = paperService.update(paper);
        Assert.assertNotEquals(0, papers.size());
    }

    @Test
    public void delete() {
        Paper paper = paperService.findById(Long.valueOf(160));
        if (paper != null){
            paperService.delete(paper);
        }

        List<Paper> papers = paperService.findAll();
        Assert.assertEquals(3, papers.size());
    }

    @Test
    public void deletePapers() {
        List<Paper> paperList = new ArrayList<>();
        Paper paper1 = new Paper();
        paper1.setId(Long.valueOf(160));
        Paper paper2 = new Paper();
        paper2.setId(Long.valueOf(170));
        paperList.add(paper1);
        paperList.add(paper2);
        paperService.deletePapers(paperList);
        List<Paper> papers = paperService.findAll();
        Assert.assertEquals(3, papers.size());
    }
}