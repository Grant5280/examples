package com.grant.mapreduce.wc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Created by Grant on 2017/12/2/002.
 */
public class WordCount {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //Get the configuration file in classpath ex:xxx-site.xml
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf,args).getRemainingArgs();
        if(otherArgs.length != 2){
            System.err.println("Args is required! ex: first is a input path , last is a output path");
            System.exit(2);
        }
        conf.set("mapred.jar","C:\\Git\\Demos\\Examples\\Hadoop_Example\\target\\Hadoop_Example-1.0-SNAPSHOT.jar");
        System.setProperty("HADOOP_USER_NAME","hadoop");
        Job job = Job.getInstance(conf,"WordCountExample");
        job.setJarByClass(WordCount.class);
        job.setMapperClass(WordCountTaskMapper.class);
        job.setCombinerClass(WordCountTaskReducer.class);
        job.setReducerClass(WordCountTaskReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        job.setSortComparatorClass(IntWritableComparator.class);
        FileInputFormat.setInputPaths(job,new Path(otherArgs[0]));
        FileOutputFormat.setOutputPath(job,new Path(otherArgs[1]));
        //job.setNumReduceTasks(3);
        System.exit(job.waitForCompletion(true)?1:2);
    }



    public static class WordCountTaskMapper extends Mapper<Object,Text,Text,IntWritable>{

        private Text k2 = new Text();
        private IntWritable v2 = new IntWritable();

        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String v1 = value.toString();
            StringTokenizer stringTokenizer = new StringTokenizer(v1);
            while (stringTokenizer.hasMoreElements()){
                k2.set(stringTokenizer.nextToken());
                v2.set(1);
                context.write(k2,v2);
            }
        }
    }

    public static class WordCountTaskReducer extends Reducer<Text,IntWritable,Text,IntWritable>{

        private IntWritable v3= new IntWritable();

        @Override
        protected void reduce(Text k2, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int num = 0;
            for (IntWritable v2: values) {
                num += v2.get();
            }
            v3.set(num);
            context.write(k2,v3);
        }
    }
}
