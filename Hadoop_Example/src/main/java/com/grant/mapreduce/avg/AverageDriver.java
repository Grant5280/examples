package com.grant.mapreduce.avg;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grant on 2017/12/14/014.
 */
public class AverageDriver {
    public static class AverageMapper extends Mapper<ObjectWritable,Text,Text,AverageTuple>{
        @Override
        protected void map(ObjectWritable key, Text value, Context context) throws IOException, InterruptedException {

        }
    }

    public static class AverageReducer extends Reducer<Text,AverageTuple,Text,AverageDriver>{
        @Override
        protected void reduce(Text key, Iterable<AverageTuple> values, Context context) throws IOException, InterruptedException {
            super.reduce(key, values, context);
        }
    }

    public static class AverageTuple extends GenericWritable{

        protected Class<? extends Writable>[] getTypes() {
            return new Class[]{Text.class,IntWritable.class,FloatWritable.class,ArrayWritable.class};
        }

        public AverageTuple(Text grade) {
            super.set(grade);
        }

        public AverageTuple(IntWritable count) {
            super.set(count);
        }

        public AverageTuple(FloatWritable avg) {
            super.set(avg);
        }
        public AverageTuple(ArrayWritable contains_stu) {
            super.set(contains_stu);
        }
        public AverageTuple() {
        }
    }
}
