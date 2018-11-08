package com.dbpj.neo4j.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Jeremy
 * @Date: 2018/11/7 15:40
 */
public class FileUtil {
    public static List<String> getAllFile(String directoryPath, boolean isAddDirectory) {
        List<String> list = new ArrayList<>();
        File baseFile = new File(directoryPath);
        if (baseFile.isFile() || !baseFile.exists()) {
            return list;
        }
        File[] files = baseFile.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                if(isAddDirectory){
                    list.add(file.getAbsolutePath());
                }
                list.addAll(getAllFile(file.getAbsolutePath(),isAddDirectory));
            } else {
                list.add(file.getAbsolutePath());
            }
        }
        return list;
    }

    public static String readFile(String path){
        BufferedReader reader = null;
        String laststr = "";
        try{
            FileInputStream fileInputStream = new FileInputStream(path);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while((tempString = reader.readLine()) != null){
                laststr += tempString;
            }
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return laststr;
    }

    // 写日志文件
    public static void writeLogFile(String path, String content){
        try {
            File errorLog = new File(path);
            FileOutputStream fos = null;
            if(!errorLog.exists()){
                errorLog.createNewFile();//如果文件不存在，就创建该文件
                fos = new FileOutputStream(errorLog);//首次写入获取
            }else{
                //如果文件已存在，那么就在文件末尾追加写入
                fos = new FileOutputStream(errorLog,true);//这里构造方法多了一个参数true,表示在文件末尾追加写入
            }

            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");//指定以UTF-8格式写入文件
            osw.write(content + "\r\n");

            //写入完成关闭流
            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
