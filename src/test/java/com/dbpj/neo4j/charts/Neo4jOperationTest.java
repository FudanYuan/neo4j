package com.dbpj.neo4j.charts;

import com.dbpj.neo4j.node.Paper;
import com.dbpj.neo4j.service.impl.PaperServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @Author: Jeremy
 * @Date: 2018/11/3 16:23
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Neo4jOperationTest {
    @Autowired
    private PaperServiceImpl paperService;

    @Test
    private void insertPaper(){
        double a = 10;
        for(int k=0; k<6; k++){
            long startTime = System.currentTimeMillis();   //获取开始时间
            for (int i=0; i<Math.pow(a, k); i++){
                Paper paper = new Paper();
                paper.setPTitle("test");
                paper.setPYear(2019);
                paper.setPAbstract("");
                paper.setPCitation(100);
                paper.setPPage(20);
                paperService.save(paper);
            }
            long endTime=System.currentTimeMillis(); //获取结束时间
            long runtime = endTime-startTime;
        }
    }
}
