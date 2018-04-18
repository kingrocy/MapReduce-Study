package com.yunhui.mapreduce.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @Author: Yun
 * @Description:
 * @Date: Created in 2018-04-18 18:00
 */
public class WordCountReduce extends Reducer<Text,IntWritable,Text,LongWritable> {

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            long count=0L;
            for(IntWritable intWritable:values){
                count+=intWritable.get();
            }
            context.write(key,new LongWritable(count));
    }
}
