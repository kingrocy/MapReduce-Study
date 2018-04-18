package com.yunhui.mapreduce.sort;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;
/**
 * @Author: Yun
 * @Description:
 * @Date: Created in 2018-04-17 18:57
 */
public class SortMapper extends Mapper<LongWritable,Text,SortBean,NullWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line=value.toString();

        String [] strs=line.split(",");

        context.write(new SortBean(strs[0],Integer.parseInt(strs[1]),Integer.parseInt(strs[2])),NullWritable.get());

    }
}
