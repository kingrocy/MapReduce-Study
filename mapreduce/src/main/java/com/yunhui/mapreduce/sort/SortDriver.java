package com.yunhui.mapreduce.sort;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * @Author: Yun
 * @Description:
 * @Date: Created in 2018-04-17 19:02
 */
public class SortDriver {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration configuration=new Configuration();

        Job job=Job.getInstance(configuration);

        job.setJarByClass(SortDriver.class);

        job.setMapperClass(SortMapper.class);
        job.setReducerClass(SortReducer.class);


        job.setMapOutputKeyClass(SortBean.class);
        job.setMapOutputValueClass(NullWritable.class);


        job.setOutputKeyClass(SortBean.class);
        job.setOutputValueClass(NullWritable.class);

        //设置分区
        job.setPartitionerClass(SortPartitioner.class);
        job.setNumReduceTasks(3);


        FileInputFormat.setInputPaths(job,new Path(args[0]));

        FileOutputFormat.setOutputPath(job,new Path(args[1]));


        boolean result = job.waitForCompletion(true);
        System.exit(result ? 0 : 1);
    }
}
