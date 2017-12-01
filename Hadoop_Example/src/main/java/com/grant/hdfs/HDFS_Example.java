package com.grant.hdfs;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;

/**
 * Created by Grant on 2017/12/1/001.
 */
public class HDFS_Example {
    private static final Logger logger= LogManager.getLogger(HDFS_Example.class);

    public static void main(String[] args) throws IOException {
        if(args.length < 1){
            logger.error("args is required! hdfs-url ex:hdfs://cluster:8020 or hdfs://namenodeserver:9000");
            System.exit(0);
        }
        String hdfs_url = args[0];
        String path = "/user/hadoop/example";
        String file_name = "hdfs_example.txt";
        String file_content = "Grant is Greatest!";
        //init hdfs file system
        Configuration cfg = new Configuration();

        /*If classpath exists xxx-site.xml the following configuration is not needed  */
       /* cfg.set("fs.defaultFS",hdfs_url);
        cfg.set("fs.hdfs.impl", DistributedFileSystem.class.getName());
        cfg.set("fs.file.impl", LocalFileSystem.class.getName());*/
        //set hadoop user
        System.setProperty("HADOOP_USER_NAME","hadoop");
        System.setProperty("hadoop.home.dir","/");
        //get FileSystem - hdfs
        FileSystem fs = FileSystem.get(URI.create(hdfs_url),cfg);

        //create folder is not exists
        Path workingDIR = fs.getWorkingDirectory();
        Path newFolder = new Path(path);
        if(!fs.exists(newFolder)){
            //Create new forder
            fs.mkdirs(newFolder);
            logger.info("Path:"+path+"  created!");
        }else{
            fs.delete(newFolder,true);
            logger.info("Path:"+path+"  deleted!");
            System.exit(0);
        }
        logger.info("write file in hdfs.");
        //create a path
        Path writepath = new Path(newFolder+"/"+file_name);
        //init OutPutStream
        FSDataOutputStream outputStream = fs.create(writepath);
        //OutPutStream usage
        outputStream.writeBytes(file_content);
        outputStream.close();
        logger.info("write hdfs successed!");

        // Read in hdfs
        Path readpath = new Path(newFolder+"/"+file_name);
        FSDataInputStream inputStream = fs.open(readpath);
        //InPutStream usage
        String out = IOUtils.toString(inputStream,"UTF-8");
        logger.info(out);
        logger.info("read hdfs successed!");
        inputStream.close();
        fs.close();
    }
}
