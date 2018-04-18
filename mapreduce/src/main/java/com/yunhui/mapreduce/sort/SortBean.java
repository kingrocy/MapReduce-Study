package com.yunhui.mapreduce.sort;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @Author: Yun
 * @Description:
 * @Date: Created in 2018-04-18 11:17
 */
public class SortBean implements WritableComparable<SortBean>{

    private String name;

    private Integer age;

    private Integer height;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public SortBean() {
        super();
    }

    public SortBean(String name, Integer age, Integer height) {
        this.name = name;
        this.age = age;
        this.height = height;
    }


    @Override
    public int compareTo(SortBean o) {
        return Integer.compare(age,o.getAge());
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(name);
        dataOutput.writeInt(age);
        dataOutput.writeInt(height);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        name=dataInput.readUTF();
        age=dataInput.readInt();
        height=dataInput.readInt();
    }

    @Override
    public String toString() {
        return  name + '\t' + age+'\t' + height;
    }
}
