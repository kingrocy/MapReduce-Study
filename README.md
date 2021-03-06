# 一、MapReduce介绍

## 1. MapReduce定义

MapReduce是一种编程模型，用于大规模数据集的并行运算，是用户开发“基于hadoop的数据分析应用”的核心框架

Mapreduce 核心功能是将用户编写的业务逻辑代码和自带默认组件整合成一个完整的分布式运算程序，并发运行在一个 hadoop 集群上。

## 2. MapReduce优缺点

### 优点
	
1. **MapReduce易于编程** 。它简单的实现一些接口，就可以完成一个分布式程序，这个
分布式程序可以分布到大量廉价的 PC 机器上运行。也就是说你写一个分布式程序，跟写一
个简单的串行程序是一模一样的。就是因为这个特点使得 MapReduce 编程变得非常流行。

2. **良好的扩展性**。当你的计算资源不能得到满足的时候，你可以通过简单的增加机器
来扩展它的计算能力。

3. **高容错性**。MapReduce 设计的初衷就是使程序能够部署在廉价的 PC 机器上，这就
要求它具有很高的容错性。比如其中一台机器挂了，它可以把上面的计算任务转移到另外一
个节点上运行，不至于这个任务运行失败，而且这个过程不需要人工参与，而完全是由
Hadoop 内部完成的。

4. **适合PB级以上海量数据的离线处理。** 

### 缺点

**MapReduce 不擅长做实时计算、流式计算、DAG（有向图）计算。**

1. **实时计算**：MapReduce 无法像 Mysql 一样，在毫秒或者秒级内返回结果。
2. **流式计算**：流式计算的输入数据是动态的，而 MapReduce 的输入数据集是静态的，
不能动态变化。这是因为 MapReduce 自身的设计特点决定了数据源必须是静态的。
3. **DAG（有向图）计算**：多个应用程序存在依赖关系，后一个应用程序的输入为前一
个的输出。在这种情况下，MapReduce 并不是不能做，而是使用后，每个 MapReduce 作业
的输出结果都会写入到磁盘，会造成大量的磁盘 IO，导致性能非常的低下。


### 3. MapReduce运行流程

![](https://raw.githubusercontent.com/kingrocy/MapReduce-Study/master/images/3.png)




### 4. MapReduce编程规范

用户编写的程序分为三个部分：Mapper、Reducer、Driver

1. **Mapper阶段**
	- 用户自定义的Mapper类需要继承Hadoop提供的Mapper父类，并重写其map()方法
	
	- Mapper的输入数据是KV对的形式
	
	- Mapper的业务逻辑写在map()方法中
	
	- Mapper的输出数据是KV对的形式
	
	- map()方法对每一个<K,V>调用一次


2. **Reducer阶段**
	
	- 用户自定义的Reducer类需要继承Hadoop提供的Reducer父类，并重写其reduce()方法
	
	- Reducer的输入数据类型对应Mapper的输出数据类型
	
	- Reducer的业务逻辑写在reduce()方法中

	- ReducerTask进程对每一组相同k的<k,v>组调用一次reduce()方法


3. **Driver阶段**

	整个MapReduce程序需要一个Driver来进行提交任务，提交的是一个描述了各种必要信息的job对象。



# 二、Hadoop的序列化

## 1、为什么要序列化？

 一般来说，"活的"对象只生存在内存里，关机断电就没有了。而且“活的”对象只能
由本地的进程使用,不能被发送到网络上的另外一台计算机。然而序列化可以存储"活的"
对象，可以将"活的"对象发送到远程计算机。

## 2、什么是序列化？

序列化就是把内存中的对象，转换成字节序列（或其他数据传输协议）以便于存储（持久化）和网络传输。

反序列化就是将收到字节序列（或其他数据传输协议）或者是硬盘的持久化数据，转换成内存中的对象。


## 3、为什么不用java的序列化？

Java 的序列化是一个重量级序列化框架（Serializable），一个对象被序列化后，会附带
很多额外的信息（各种校验信息，header，继承体系等），不便于在网络中高效传输。所以，
hadoop 自己开发了一套序列化机制（Writable），精简、高效。


## 4、hadoop常用的数据序列化类型

<table>	
		<tr>
			<td>Java类型</td>
			<td>Hadoop类型</td>
		</tr>
		<tr>
			<td>boolean</td>
			<td>BooleanWritable</td>
		</tr>
		<tr>
			<td>byte</td>
			<td>ByteWritable</td>
		</tr>
		<tr>
			<td>int</td>
			<td>IntWritable</td>
		</tr>
		<tr>
			<td>float</td>
			<td>FloatWritable</td>
		</tr>
		<tr>
			<td>long</td>
			<td>LongWritable</td>
		</tr>
		<tr>
			<td>double</td>
			<td>DoubleWritable</td>
		</tr>
		<tr>
			<td>string</td>
			<td>Text</td>
		</tr>
		<tr>
			<td>map</td>
			<td>MapWritable</td>
		</tr>
		<tr>
			<td>array</td>
			<td>ArrayWritable</td>
		</tr>
</table>


## 5、自定义java对象实现Hadoop的序列化（实现Writable接口）

**步骤：**

- **提供空参构造函数**（hadoop反序列化创建对象时调用）

		public SortBean() {
	        super();
	    }

- **实现Writable接口，重写序列化和反序列化方法**

		
		//此处有个注意事项：序列化与反序列化时对对象属性的读写顺序要保持一致
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

- **重写toString()方法**

	    @Override
	    public String toString() {
	        return  name + '\t' + age+'\t' + height;
	    }


# 三、MapReduce编程

## 1、window本地Hadoop运行环境搭建

window下如果想本地运行hadoop程序，需要配置HADOOP_HOME。首先我们下载 hadoop-common-2.7.1-bin-master 并解压。然后添加环境变量，如下(*这里我们需要注意一下：我们配置的环境变量里面的hadoop的版本是2.7，所以我们开发时使用的hadoop的jar包也必须是2.7的*)：

![](https://raw.githubusercontent.com/kingrocy/MapReduce-Study/master/images/1.png)

修改Path环境变量 如下：

![](https://github.com/kingrocy/MapReduce-Study/blob/master/images/2.png)

经过上述步骤之后，我们就能愉快的在本地进行MapReduce程序调试了。。。*至于Mac系统如何本地运行MR程序，请自行百度*。。。


## 2、编程实战 

一般来说，我们用到MapReduce最多的就是**统计**、**排序**、**分区**这三个知识点了，，下面给出三个案例来学习。

在码代码前，必要的工作还是先得提起说的。。首先我们得引入MapReduce开发所需要的jar包

	<dependency>
	    <groupId>org.apache.hadoop</groupId>
	    <artifactId>hadoop-mapreduce-client-jobclient</artifactId>
	    <version>2.7.0</version>
    </dependency>

    <dependency>
        <groupId>org.apache.hadoop</groupId>
        <artifactId>hadoop-common</artifactId>
        <version>2.7.0</version>
    </dependency>

    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-core</artifactId>
        <version>2.7</version>
    </dependency>

本地开发时，还需导入log4j的配置文件，才能看到MapReduce程序执行的情况。

	log4j.rootLogger=INFO, stdout
	log4j.appender.stdout=org.apache.log4j.ConsoleAppender
	log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
	log4j.appender.stdout.layout.ConversionPattern=%d %p [%c] - %m%n

这样子我们的准备工作就已经做完了。。正式开始码代码


### 1、WordCount案例

1. 自定义的Mapper

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


2. 自定义的Reducer

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

3. Driver

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




### 2、对于不同的人按身高或年龄排序

1. 自定义bean  作为key

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


2. Mapper

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


3. Reducer

		package com.yunhui.mapreduce.sort;
		import org.apache.hadoop.io.NullWritable;
		import org.apache.hadoop.mapreduce.Reducer;
		import java.io.IOException;
		/**
		 * @Author: Yun
		 * @Description:
		 * @Date: Created in 2018-04-17 19:01
		 */
		public class SortReducer extends Reducer<SortBean,NullWritable,SortBean,NullWritable> {
		
		    @Override
		    protected void reduce(SortBean key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
		           context.write(key,values.iterator().next());
		    }
		}


4. Driver

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
		
		        FileInputFormat.setInputPaths(job,new Path(args[0]));
		        FileOutputFormat.setOutputPath(job,new Path(args[1]));
		        
		        boolean result = job.waitForCompletion(true);
		        System.exit(result ? 0 : 1);
		    }
		}





### 3、对于不同的人按姓分区

首先先说一下MapReduce默认的分区规则是HashPartitioner,其分区方法如下：

    public int getPartition(K key, V value, int numReduceTasks) {
        return (key.hashCode() & 2147483647) % numReduceTasks;
    }


我们如果想要自定义分区，需要继承Partitioner，重写其分区方法。

这里我们是基于第二个Demo 将不同姓氏的人输出到不同的文件

1. 自定义Partioner

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


2. 在Driver设置自定义Partioner作为分区类

        //设置分区
        job.setPartitionerClass(SortPartitioner.class);
        job.setNumReduceTasks(3);


















