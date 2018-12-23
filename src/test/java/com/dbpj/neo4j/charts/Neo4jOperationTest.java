package com.dbpj.neo4j.charts;

import com.dbpj.neo4j.node.Conference;
import com.dbpj.neo4j.node.Paper;
import com.dbpj.neo4j.relation.PaperConferenceRelation;
import com.dbpj.neo4j.repository.AuthorPaperRelationRepository;
import com.dbpj.neo4j.repository.AuthorRepository;
import com.dbpj.neo4j.repository.PaperConferenceRelationRepository;
import com.dbpj.neo4j.repository.PaperRepository;
import com.dbpj.neo4j.service.impl.ConferenceServiceImpl;
import com.dbpj.neo4j.service.impl.PaperServiceImpl;
import com.dbpj.neo4j.utils.FileUtil;
import com.dbpj.neo4j.vo.AuthorPartnersRankByPaperVO;
import com.dbpj.neo4j.vo.AuthorRankByPaperVO;
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
public class Neo4jOperationTest {
    @Autowired
    private PaperServiceImpl paperService;

    @Autowired
    private ConferenceServiceImpl conferenceService;

    @Autowired
    private AuthorPaperRelationRepository authorPaperRelationRepository;

    @Autowired
    private PaperConferenceRelationRepository paperConferenceRelationRepository;

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private AuthorRepository authorRepository;

    // 论文插入时间结果
    private final String insertPaperResultPath = "insert_paper_neo4j.csv";

    // 论文删除时间结果
    private final String deletePaperResultPath = "delete_paper_neo4j.csv";

    // 作者论文每年的数量结果
    private final String authorAndPaperCountByYearResultPath = "author_paper_count_by_year_neo4j.csv";

    // 作者论文每年的数量结果
    private final String paperRateByConferenceResultPath = "paper_rate_by_conference_neo4j.csv";

    // 引用数排名前K的论文
    private final String paperCitationRankResultPath = "paper_citation_rank_neo4j.csv";

    // 发表论文总数排名前K的作者
    private final String authorRankByPaperCountResultPath = "author_rank_by_paper_count_neo4j.csv";

    // 合作论文次数排名前10的作者组合
    private final String authorPartnersByPaperCountResultPath = "author_partners_by_paper_count_neo4j.csv";

    // 测试次数
    private final Integer testTime = 10;

    // 指数量级
    private final Integer magnitude = 2;

    /**
     * 测试插入、删除论文
     */
    @Test
    public void insertAndDeletePaper(){
        // 论文摘要
        String pAbstract = "Multilingual parallel text corpora provide a powerful means for propagating linguistic knowledge across languages. We present a model which jointly learns linguistic structure for each language while inducing links between them. Our model supports fully symmetrical knowledge transfer, utilizing any combination of supervised and unsupervised data across language barriers. The proposed non-parametric Bayesian model effectively combines cross-lingual alignment with target language predictions. This architecture is a potent alternative to projection methods which decompose these decisions into two separate stages. We apply this approach to the task of morphological segmentation, where the goal is to separate a word into its individual morphemes. When tested on a parallel corpus of Hebrew and Arabic, our joint bilingual model effectively incorporates all available evidence from both languages, yielding significant performance gains.";
        pAbstract.replaceAll("\"", "").replaceAll("\\\\", "");
        // 写入header
        String header = "count,1,10,100,1000,10000,100000,1000000";
        FileUtil.writeLogFile(insertPaperResultPath, header, false);
        FileUtil.writeLogFile(deletePaperResultPath, header, false);

        for(int t=0; t<testTime; t++){
            String rowInsert = String.valueOf(t) + ",";
            String rowDelete = String.valueOf(t) + ",";
            double a = 10;
            for(int k=0; k<magnitude; k++){
                List<Paper> paperList = new ArrayList<>();
                // 插入
                // 获取开始时间
                long startTime = System.currentTimeMillis();
                for (int i=0; i<Math.pow(a, k); i++){
                    Paper paper = new Paper();
                    paper.setPTitle("testPaper");
                    paper.setPYear(2019);
                    paper.setPAbstract(pAbstract);
                    paper.setPCitation(100);
                    paper.setPPage(20);
                    paperList.add(paperService.save(paper).get(0));
                }
                // 获取结束时间
                long endTime = System.currentTimeMillis();
                long runtime = endTime-startTime;
                rowInsert += String.valueOf(runtime);
                if (k < magnitude-1){
                    rowInsert += ",";
                }

                // 删除论文
                startTime = System.currentTimeMillis();
                for(Paper p : paperList){
                    paperService.delete(p);
                }
                endTime = System.currentTimeMillis();
                runtime = endTime-startTime;
                rowDelete += String.valueOf(runtime);
                if (k < magnitude-1){
                    rowDelete += ",";
                }
            }
            FileUtil.writeLogFile(insertPaperResultPath, rowInsert, true);
            FileUtil.writeLogFile(deletePaperResultPath, rowDelete, true);
        }
    }


    /**
     * 测试作者数和论文数随年份的变化
     */
    @Test
    public void authorAndPaperCountByYear(){
        // 写入header
        String header = "type/time,";
        for (int y = 2008; y < 2019; y++){
            header += String.valueOf(y);
            if (y < 2018){
                header += ",";
            }
        }
        FileUtil.writeLogFile(authorAndPaperCountByYearResultPath, header, false);
        List<String> rowTimeList = new ArrayList<>();
        String rowAuthorCount = "authorCount,";
        String rowPaperCount = "paperCount,";
        for (int t = 0; t < testTime; t++){
            String rowTime = String.valueOf(t) + ",";
            for (int y = 2008; y < 2019; y++){
                // 获取开始时间
                long startTime = System.currentTimeMillis();
                Long a = authorPaperRelationRepository.findAuthorCountByYear(y);
                Long b = authorPaperRelationRepository.findPaperCountByYear(y);
                if (t == 0){
                    rowAuthorCount += String.valueOf(a);
                    rowPaperCount += String.valueOf(b);
                    if (y < 2018){
                        rowAuthorCount += ",";
                        rowPaperCount += ",";
                    }
                }

                // 获取结束时间
                long endTime = System.currentTimeMillis();
                long runtime = endTime-startTime;
                rowTime += String.valueOf(runtime);
                if (y<2018){
                    rowTime += ",";
                }
            }
            rowTimeList.add(rowTime);
        }

        FileUtil.writeLogFile(authorAndPaperCountByYearResultPath, rowAuthorCount, true);
        FileUtil.writeLogFile(authorAndPaperCountByYearResultPath, rowPaperCount, true);
        for (int i=0; i<rowTimeList.size(); i++){
            FileUtil.writeLogFile(authorAndPaperCountByYearResultPath, rowTimeList.get(i), true);
        }
    }

    @Test
    public void paperRateByConference(){
        // 写入header
        String header = "type/time,";
        List<Conference> conferenceList = conferenceService.findAll();
        int cSize = conferenceList.size();

        List<String> cNameList = new ArrayList<>();
        for (int i=0; i<cSize; i++){
            String cName = conferenceList.get(i).getCName();
            cNameList.add(cName);

            header +=  cName;
            if (i < cSize - 1){
                header += ",";
            }
        }
        FileUtil.writeLogFile(paperRateByConferenceResultPath, header, false);

        String rowCountTime1 = "2008-2013";
        String rowCountTime2 = "2014-2018";
        List<String> rowTimeList = new ArrayList<>();
        for (int t=0; t<testTime; t++){
            String rowTime = String.valueOf(t) + ",";
            for (int i=0; i<cSize; i++){
                String cName = cNameList.get(i);
                // 获取开始时间
                long startTime = System.currentTimeMillis();
                Long a = paperConferenceRelationRepository.findPaperCountByConferenceAndPYearBetween(cName, 2008, 2013);
                Long b = paperConferenceRelationRepository.findPaperCountByConferenceAndPYearBetween(cName, 2014, 2018);
                if (t == 0){
                    rowCountTime1 += String.valueOf(a);
                    rowCountTime2 += String.valueOf(b);
                    if (i<cSize-1){
                        rowCountTime1 += ",";
                        rowCountTime2 += ",";
                    }
                }
                // 获取结束时间
                long endTime = System.currentTimeMillis();
                long runtime = endTime-startTime;
                rowTime += String.valueOf(runtime);
                if (i<cSize-1){
                    rowTime += ",";
                }
            }
            rowTimeList.add(rowTime);
        }
        FileUtil.writeLogFile(paperRateByConferenceResultPath, rowCountTime1, true);
        FileUtil.writeLogFile(paperRateByConferenceResultPath, rowCountTime2, true);
        for (int i=0; i<rowTimeList.size(); i++){
            FileUtil.writeLogFile(paperRateByConferenceResultPath, rowTimeList.get(i), true);
        }
    }

    /**
     * 引用数量排名前k的论文
     */
    @Test
    public void paperCitationRank(){

        int limit = 100;

        // 写入header
        String header = "title,";
        String citationCount = "citation,";
        List<String> rowTimeList = new ArrayList<>();
        for (int t=0; t<testTime; t++){
            String rowTime = String.valueOf(t) + ",";
            // 获取开始时间
            long startTime = System.currentTimeMillis();
            List<Paper> paperList = paperRepository.findByPCitationCountLimit(limit);
            // 获取结束时间
            long endTime = System.currentTimeMillis();
            long runtime = endTime-startTime;
            if (t==0){
                for (int i=0; i<paperList.size(); i++){
                    String title = paperList.get(i).getPTitle();
                    header += title;
                    citationCount += paperList.get(i).getPCitation();
                    if (i < paperList.size()-1){
                        header += ",";
                        citationCount += ",";
                    }
                }
            }
            rowTime += String.valueOf(runtime);
            rowTimeList.add(rowTime);
        }

        FileUtil.writeLogFile(paperCitationRankResultPath, header, false);
        FileUtil.writeLogFile(paperCitationRankResultPath, citationCount, true);
        for (int i=0; i<rowTimeList.size(); i++){
            FileUtil.writeLogFile(paperCitationRankResultPath, rowTimeList.get(i), true);
        }
    }

    /**
     * 发表论文数量排名前10的作者
     */
    @Test
    public void authorByPaperCountLimit(){

        int limit = 10;

        // 写入header
        String header = "author,";
        String paperCount = "paperCount,";
        List<String> rowTimeList = new ArrayList<>();
        for (int t=0; t<testTime; t++){
            String rowTime = String.valueOf(t) + ",";
            // 获取开始时间
            long startTime = System.currentTimeMillis();
            List<AuthorRankByPaperVO> authorRankByPaperVOList = authorRepository.findAuthorRankByPaper(limit);
            // 获取结束时间
            long endTime = System.currentTimeMillis();
            long runtime = endTime-startTime;
            if (t==0){
                for (int i=0; i<authorRankByPaperVOList.size(); i++){
                    String aName = authorRankByPaperVOList.get(i).getAuthor().getAName();
                    header += aName;
                    paperCount += authorRankByPaperVOList.get(i).getPaperCount().toString();
                    if (i < authorRankByPaperVOList.size()-1){
                        header += ",";
                        paperCount += ",";
                    }
                }
            }
            rowTime += String.valueOf(runtime);
            rowTimeList.add(rowTime);
        }

        FileUtil.writeLogFile(authorRankByPaperCountResultPath, header, false);
        FileUtil.writeLogFile(authorRankByPaperCountResultPath, paperCount, true);
        for (int i=0; i<rowTimeList.size(); i++){
            FileUtil.writeLogFile(authorRankByPaperCountResultPath, rowTimeList.get(i), true);
        }
    }

    /**
     * 合作论文次数排名前10的作者组合
     */
    @Test
    public void authorPartnerByPaperCountLimit(){
        // 实质上是查询前10，因为作者对1-2和2-1是一回事。
        int limit = 20;

        // 写入header
        String header = "author,";
        String paperCount = "paperCount,";
        List<String> rowTimeList = new ArrayList<>();
        for (int t=0; t<testTime; t++){
            String rowTime = String.valueOf(t) + ",";
            // 获取开始时间
            long startTime = System.currentTimeMillis();
            List<AuthorPartnersRankByPaperVO> authorPartnersRankByPaperVOList = authorRepository.findAuthorPartnersRankByPaper(limit);
            // 获取结束时间
            long endTime = System.currentTimeMillis();
            long runtime = endTime-startTime;
            if (t==0){
                for (int i=0; i<authorPartnersRankByPaperVOList.size(); i+=2){
                    String aName = authorPartnersRankByPaperVOList.get(i).getAuthor1().getAName() +
                            "-" + authorPartnersRankByPaperVOList.get(i).getAuthor2().getAName();
                    header += aName;
                    paperCount += authorPartnersRankByPaperVOList.get(i).getPaperCount().toString();
                    if (i < authorPartnersRankByPaperVOList.size()-1){
                        header += ",";
                        paperCount += ",";
                    }
                }
            }
            rowTime += String.valueOf(runtime);
            rowTimeList.add(rowTime);
        }

        FileUtil.writeLogFile(authorPartnersByPaperCountResultPath, header, false);
        FileUtil.writeLogFile(authorPartnersByPaperCountResultPath, paperCount, true);
        for (int i=0; i<rowTimeList.size(); i++){
            FileUtil.writeLogFile(authorPartnersByPaperCountResultPath, rowTimeList.get(i), true);
        }
    }
}
