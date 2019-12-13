package me.danght.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * 测试 HDFS Client
 * @author DangHT
 * @date 11/12/2019
 */
public class HDFSClientTest {

    Properties properties;

    static String HOST;
    static String PORT;
    static String USER;

    /**
     * 加载HDFS配置文件
     */
    @Before
    public void loadProperties() {
        properties = new Properties();
        InputStream in = getClass().getResourceAsStream("/hdfs.properties");
        try {
            properties.load(in);
            HOST = properties.getProperty("host");
            PORT = properties.getProperty("port");
            USER = properties.getProperty("user");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * HDFS创建文件夹
     */
    @Test
    public void mkdirs() throws IOException, URISyntaxException, InterruptedException {
        // 1.获取文件系统
        Configuration conf = new Configuration();

        // 配置在集群上运行
        conf.set("fs.defaultFS", "hdfs://" + HOST + ":" + PORT);
        FileSystem fs = FileSystem.get(new URI("hdfs://" + HOST + ":" + PORT), conf, USER);

        // 2.创建目录
        fs.mkdirs(new Path("/movie/Joker"));

        // 3.关闭资源
        fs.close();
    }

    /**
     * HDFS文件删除
     */
    @Test
    public void delete() throws IOException, URISyntaxException, InterruptedException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://" + HOST + ":" + PORT), conf, USER);

        fs.delete(new Path("/movie/Joker/"), true);
        fs.close();
    }

    /**
     * HDFS文件重命名
     */
    @Test
    public void rename() throws IOException, URISyntaxException, InterruptedException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://" + HOST + ":" + PORT), conf, USER);

        fs.rename(new Path("/testRename.txt"),
                new Path("/renameTest.txt"));
        fs.close();
    }

    /**
     * HDFS文件详情查看
     */
    @Test
    public void listFiles() throws URISyntaxException, IOException, InterruptedException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://" + HOST + ":" + PORT), conf, USER);

        RemoteIterator<LocatedFileStatus> listFiles = fs.listFiles(new Path("/"), true);

        while (listFiles.hasNext()) {
            LocatedFileStatus status = listFiles.next();

            System.out.println(status.getPath().getName());
            System.out.println(status.getLen());
            System.out.println(status.getPermission());
            System.out.println(status.getGroup());

            BlockLocation[] blockLocations = status.getBlockLocations();
            for (BlockLocation location : blockLocations) {
                String[] hosts = location.getHosts();
                for (String host : hosts) {
                    System.out.println(host);
                }
            }

            System.out.println("-------------------------------------");
        }

        fs.close();
    }

    /**
     * HDFS判断文件和目录
     */
    @Test
    public void listStatus() throws URISyntaxException, IOException, InterruptedException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://" + HOST + ":" + PORT), conf, USER);

        FileStatus[] fileStatuses = fs.listStatus(new Path("/"));
        for (FileStatus status : fileStatuses) {
            if (status.isFile()) {
                System.out.println("file: " + status.getPath().getName());
            } else {
                System.out.println("directory: " + status.getPath().getName());
            }
        }

        fs.close();
    }

    /**
     * 文件IO流：文件上传
     */
    @Test
    public void putFileToHDFS() throws IOException, URISyntaxException, InterruptedException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://" + HOST + ":" + PORT), conf, USER);

        FileInputStream fis = new FileInputStream(
                new File(HDFSClientTest.class.getClassLoader().getResource("hdfs.properties").getPath()));
        FSDataOutputStream fos = fs.create(new Path("/hdfs.properties"));

        IOUtils.copyBytes(fis, fos, conf);
        IOUtils.closeStream(fos);
        IOUtils.closeStream(fis);
        fs.close();
    }

    /**
     * 文件IO流：文件下载
     */
    @Test
    public void getFileFromHDFS() throws URISyntaxException, IOException, InterruptedException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://" + HOST + ":" + PORT), conf, USER);

        FSDataInputStream fis = fs.open(new Path("/hdfs.properties"));

        FileOutputStream fos = new FileOutputStream(
                new File(".", "hdfs_dup.properties"));

        IOUtils.copyBytes(fis, fos, conf);
        IOUtils.closeStream(fos);
        IOUtils.closeStream(fis);
        fs.close();
    }

    /**
     * 定位文件读取：下载第一块
     */
    @Test
    public void readFileSeek1() throws URISyntaxException, IOException, InterruptedException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://" + HOST + ":" + PORT), conf, USER);

        FSDataInputStream fis = fs.open(new Path("/hadoop-2.7.7.tar.gz"));

        FileOutputStream fos = new FileOutputStream(new File("./hadoop-2.7.7.tar.gz.part1"));

        byte[] buf = new byte[1024];

        for (int i = 0; i < 1024 * 128; i++) {
            fis.read(buf);
            fos.write(buf);
        }

        IOUtils.closeStream(fis);
        IOUtils.closeStream(fos);
        fs.close();
    }

    /**
     * 定位文件读取：下载第二块
     */
    @Test
    public void readFileSeek2() throws URISyntaxException, IOException, InterruptedException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://" + HOST + ":" + PORT), conf, USER);

        FSDataInputStream fis = fs.open(new Path("/hadoop-2.7.7.tar.gz"));

        fis.seek(1024 * 1024 * 128);

        FileOutputStream fos = new FileOutputStream(
                new File("./hadoop-2.7.7.tar.gz.part2"));

        IOUtils.copyBytes(fis, fos, conf);
        IOUtils.closeStream(fis);
        IOUtils.closeStream(fos);
        fs.close();
    }

}
