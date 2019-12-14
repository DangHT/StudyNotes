# :card_file_box: HDFS

Hadoop Distributed File System (HDFS)，顾名思义是一个**分布式文件系统**，它是Hadoop的核心之一。

HDFS是一个可以在**低成本**机器上实现**高容错**的分布式文件系统，并且支持**大容量**文件存储和**流式**数据访问。

## 优缺点

优点

- 高容错性：通过保存数据副本的方法提高容错性
- 可处理大容量数据
- 可以构建在低成本机器上

缺点

- 不适合低延时数据访问，比如毫秒级的存储数据是做不到的
- 无法高效的对大量的小文件进行存储
  - 存储大量小文件会占用 NameNode 大量的内存空间来记录文件的目录和块信息
  - 小文件存储的寻址时间会超过读取时间，这违反了HDFS的设计原则
- 不支持并发写入、随机修改
  - 一个文件只能有一个写，不允许多个线程同时写
  - 仅支持 append（追加），不支持文件的随机修改

## 架构设计

HDFS基于 master/slave（主/从）架构。一般情况下，一个HDFS集群由一个 NameNode 和多个 DataNode 组成。（为了保证高可用，还会有一个 SecondaryNameNode，会在后文讲解）



![](https://raw.githubusercontent.com/DangHT/StudyNotes/master/Hadoop/HDFS/static/images/hdfsarchitecture.jpg)

**NameNode**

- 管理HDFS的名称空间
- 配置副本策略
- 管理数据块映射信息
- 处理客户端读写请求

**DataNode**

- 存储实际的数据块
- 执行数据块的读写操作

**Client**

- 文件切分。文件上传的时候，Client将文件切分成一个一个的Block，然后再上传
- 与NameNode交互，获取文件的位置信息
- 与DataNode交互，读取或写入数据
- Client提供一些命令来管理HDFS，比如NameNode格式化
- Client可以通过一些命令来访问HDFS，比如对HDFS增删改查

**Secondary NameNode**

- 辅助NameNode，分担其工作量，定期合并Fsimage和Edits，并推送给NameNode
- 在紧急情况下，可以辅助恢复NameNode

## HDFS文件块（Block）

HDFS中的文件在物理上按照分块（Block）存储，块大小可以通过参数 dfs.blocksize 来进行配置

**默认大小：Hadoop 2.X之前 64M，Hadoop 2.X之后 128M**

HDFS块大小设置的**太小**，**会增加寻址时间**

HDFS块大小设置的**太大**，**会增加从磁盘传输数据的时间**

因此，**HDFS块大小的设置主要取决于磁盘传输速率，最佳状态为：寻址时间为传输时间的1%**

原始数据会根据设置的块大小进行切分，**除了最后一个块以外，所有的块都是一样大的**，Hadoop也支持可变长块，这样就可以在不填充最后一个块的情况下开始新的块。

## 数据备份

NameNode负责有关数据备份的所有决定。它会定期地从集群中的每一个DataNode接收心跳信号和Blockreport。

收到心跳信号表示DataNode正常运行。Blockreport上包含了DataNode上所有的块信息

![](https://raw.githubusercontent.com/DangHT/StudyNotes/master/Hadoop/HDFS/static/images/hdfsdatanodes.jpg)

### 备份放置策略

数据备份的放置策略是HDFS保证高可用性的关键。

**HDFS将 1/3 的文件副本放在 Client 所在的机架的节点上，1/3 的文件副本放在同一机架的另一节点上，另外 1/3 的副本放在不同机架的节点上**

例如，如果设置的文件备份数为 3 个，那么就会这样放置备份：

![](https://raw.githubusercontent.com/DangHT/StudyNotes/master/Hadoop/HDFS/static/images/replication.jpg)

当处理读取请求时，HDFS会优先选择与Client最近的副本节点读取数据

## HDFS写数据流程

![](https://raw.githubusercontent.com/DangHT/StudyNotes/master/Hadoop/HDFS/static/images/hdfswrite.jpg)

1. 客户端调用DistributedFileSystem（以下简称DFS）的create()方法。DFS向NameNode发送一个RPC调用，请求创建一个新文件（此时不包含Block请求）。
2. NameNode收到RPC调用后，会进行一系列检查，如用户是否具有权限，文件是否已经存在等。所有检查通过后，会在edits中记录新建一个文件日志（关于edits后文详解），然后通知DFS允许上传。DFS收到确认后，会返回一个FSDataOutputStream对象，其中封装了一个DFSOutputStream对象用于客户端与NameNode和DataNode通信
3. 客户端开始写文件，DFSOutputStream会将文件分割成packet数据包，并写入一个内部的data queue中。DataStreamer会读取其中的内容，并请求NameNode返回一个DataNode列表来存储当前的Block副本。
4. NameNode收到请求后，会根据备份放置策略返回一个DataNode列表
5. FSDataOutputStream会从DataNode列表中选择最近的DataNode发送建立传输通道Pipeline请求，这个DataNode会继续向下一个最近的DataNode请求建立Pipeline，一直向下进行
6. 最后一个DataNode收到请求后再递归的回复确认建立消息，直到FSDataOutputStream收到后，通道建立成功
7. DFSOutputStream开始向第一个DataNode写数据，DataNode收到数据后会一边将数据持久化存储在本地一边向下一个DataNode发送数据
8. DFSOutputStream在内部还会维护一个ack queue，其中包含所有已经发出的packet。当最后一个DataNode写完数据后会递归地回复应答消息，收到回复的packet会从ack queue中删除掉。全部删除表示传输成功。
9. 接着DFS会向NameNode发送消息表示此Block已经传输成功，请求传输下一个Block（重复步骤3~8）直到传输完成

如果在写数据的过程中出现DataNode失效了怎么办呢？

首先，Pipeline会关闭，ack queue中剩余的packets会被添加到data queue前面以确保不会丢失数据

然后，在正常的DataNode上已经保存好的Block版本会升级，这样在失效DataNode恢复后可以自动删除旧版本

最后，将剩余的数据写入正常的DataNode中，只要成功写入的Block副本数达到dfs.replication.min（默认为1），就认为写入成功，NameNode会在后续的检查中，通过异步复制的方式补充复制副本数不足的Block

## HDFS读数据流程

![](https://raw.githubusercontent.com/DangHT/StudyNotes/master/Hadoop/HDFS/static/images/hdfsread.jpg)

1. 客户端通过DFS向NameNode请求下载目标文件
2. NameNode查询MetaData找到目标文件所在的分块信息和DataNode地址并返回
3. 客户端按照就近原则选择一台DataNode请求传输数据
4. DataNode根据客户端请求下载的数据块找到并返回
5. 客户端以Packet为单位接收文件，先在本地缓存，然后写入目标文件

## 参考资料

> [1] [Hadoop-2.7.7 HDFS Architecture](https://hadoop.apache.org/docs/r2.7.7/hadoop-project-dist/hadoop-hdfs/HdfsDesign.html)
>
> [2] 尚硅谷大数据之Hadoop
>
> [3] [Hadoop深入学习：解析HDFS的写文件流程](https://www.iteye.com/blog/flyingdutchman-1900536)
>
> [4] [HDFS写文件流程（详细必看）](https://zhuanlan.zhihu.com/p/66051354)