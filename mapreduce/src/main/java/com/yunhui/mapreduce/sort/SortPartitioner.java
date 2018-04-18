package com.yunhui.mapreduce.sort;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * @Author: Yun
 * @Description:
 * @Date: Created in 2018-04-18 14:12
 */
public class SortPartitioner extends Partitioner<SortBean,NullWritable> {

    @Override
    public int getPartition(SortBean sortBean, NullWritable nullWritable, int i) {
        String name=sortBean.getName();
        if(name.startsWith("王")){
            return 0;
        }else if(name.startsWith("陈")){
            return 1;
        }else{
            return 2;
        }
    }
}
