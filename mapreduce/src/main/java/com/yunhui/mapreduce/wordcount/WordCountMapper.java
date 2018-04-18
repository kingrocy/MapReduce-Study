package com.yunhui.mapreduce.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @Author: Yun
 * @Description:
 * @Date: Created in 2018-04-18 17:57
 */
public class WordCountMapper extends Mapper<LongWritable,Text,Text,IntWritable> {

    Text k=new Text();

    IntWritable v=new IntWritable(1);

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line=value.toString();

        String [] strs=line.split(",");

        for(String str:strs){
            k.set(str);
            context.write(k,v);
        }

    }
}
