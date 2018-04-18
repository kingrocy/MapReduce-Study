package com.yunhui.mapreduce.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * @Author: Yun
 * @Description:
 * @Date: Created in 2018-04-18 18:24
 */
public class WordCountPatitioner extends Partitioner<Text,IntWritable> {

    @Override
    public int getPartition(Text text, IntWritable intWritable, int i) {
        return Math.abs(text.getLength()%2);
    }
}
