package com.yunhui.mapreduce.wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * @Author: Yun
 * @Description:
 * @Date: Created in 2018-04-18 18:02
 */
public class WordCountDriver {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //创建job
        Configuration configuration=new Configuration();
        Job job= Job.getInstance(configuration);

        //设置jar
        job.setJarByClass(WordCountDriver.class);

        //设置Map类
        job.setMapperClass(WordCountMapper.class);

        //设置Reducer类
        job.setReducerClass(WordCountReduce.class);

        //设置Map的输出K V类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        //设置Reducer的输出K V类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        //设置输入输出路径
        FileInputFormat.setInputPaths(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job,new Path(args[1]));
        
        //提交job 为true代表任务执行成功  为false代表执行失败
        boolean result = job.waitForCompletion(true);

        System.exit(result?0:1);


    }
}
