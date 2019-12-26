package com.wzw.test;

import org.apache.log4j.Logger;
import org.apache.zookeeper.*;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;


public class WatchOne {

    private static final Logger logger=Logger.getLogger(WatchOne.class);

    //zookeeper的连接地址
    private final static String CONNECTSTRING="192.168.1.102";

    private final static String PATH="/node1";
    //超时时间
    private final static int SESSION_TIMEOUT=5000*1000;

    private ZooKeeper zk=null;

    public ZooKeeper startZK() throws IOException {
        return new ZooKeeper(CONNECTSTRING, SESSION_TIMEOUT, new Watcher() {
            public void process(WatchedEvent watchedEvent) {

            }
        });
    }


    //创建节点
    public void createZnode(String nodePath,String nodeValue) throws KeeperException, InterruptedException {
        //参数1：节点名称；参数2：节点的值；参数3,：权限，因为Linux已经帮我们做了，这里写这个就好；参数4：该是否临时节点
        zk.create(nodePath,nodeValue.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    //得到节点的值
    public String getZnode(String nodePath) throws KeeperException, InterruptedException {
        String result=null;
        byte[] byteArray=zk.getData(PATH, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                try {
                    trigetValue(PATH);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, new Stat());
        //字节转成String
        result=new String(byteArray);
        return result;
    }

    private String trigetValue(String path) throws KeeperException, InterruptedException {
        String result=null;
        byte[] byteArray=zk.getData(PATH, false, new Stat());
        //字节转成String
        result=new String(byteArray);
        logger.info("**************watch ont time:"+result);
        return result;
    }

    /**
     * 监控节点，获得初值之后设置watch，只要发生变化，打印最新的值，一次性watch
     * @param args
     * @throws IOException
     * @throws InterruptedException
     * @throws KeeperException
     */
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        WatchOne watchOne=new WatchOne();

        watchOne.setZk(watchOne.startZK());

        if(watchOne.getZk().exists(PATH,false)==null){
            watchOne.createZnode(PATH,"dfvdsvcxvcxrer");
            String result=watchOne.getZnode(PATH);
            logger.info("**************first retValue:"+result);
            Thread.sleep(Long.MAX_VALUE);
        }else{
            logger.info("**************node ok:");
        }


    }


    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }
}
