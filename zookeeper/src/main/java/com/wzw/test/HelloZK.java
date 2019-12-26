package com.wzw.test;

import javafx.scene.shape.Path;
import org.apache.log4j.Logger;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.ZooDefs.Ids;
import java.io.IOException;
import java.util.List;


public class HelloZK {

    private static final Logger logger=Logger.getLogger(HelloZK.class);

    //zookeeper的连接地址
    private final static String CONNECTSTRING="192.168.43.223";

    private final static String PATH="/aaassss";
    //超时时间
    private final static int SESSION_TIMEOUT=5000*1000;

    public ZooKeeper startZK() throws IOException {
        return new ZooKeeper(CONNECTSTRING, SESSION_TIMEOUT, new Watcher() {
            public void process(WatchedEvent watchedEvent) {

            }
        });
    }

    public void stopZK(ZooKeeper zk) throws InterruptedException {
        if(null!=zk)
        zk.close();
    }

    //创建节点
    public void createZnode(ZooKeeper zk,String nodePath,String nodeValue) throws KeeperException, InterruptedException {
        //参数1：节点名称；参数2：节点的值；参数3,：权限，因为Linux已经帮我们做了，这里写这个就好；参数4：该是否临时节点
        zk.create(nodePath,nodeValue.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    //得到节点的值
    public String getZnode(ZooKeeper zk, String nodePath) throws KeeperException, InterruptedException {
        String result=null;
        byte[] byteArray=zk.getData(nodePath,false,new Stat());
        //字节转成String
        result=new String(byteArray);
        return result;
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        HelloZK hello=new HelloZK();
        ZooKeeper zk=hello.startZK();
        //zk.exists(PATH,false),要先判断这个节点是否存在，如果存在肯定要报错，不存在再创建
        if(zk.exists(PATH,false)==null) {
            hello.createZnode(zk, PATH, "helloNode1");
            String retValue = hello.getZnode(zk, PATH);
            logger.info("**********************retValue:"+retValue);
            System.out.println("**********************retValue:"+retValue);
        }
        else{
            logger.info("**********************i have this node");
            System.out.println("**********************i have this node");
        }
        hello.stopZK(zk);
    }

}
