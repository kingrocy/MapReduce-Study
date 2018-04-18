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


### 3. MapReduce核心思想


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

### 2、对于不同的人按身高或年龄排序

### 3、对于不同的人按姓分区

















