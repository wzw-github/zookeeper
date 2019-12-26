package com.wzw.test;

import org.apache.log4j.Logger;
import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.EventType;

import java.io.IOException;
import java.util.List;


public class WatchChild {

    private static final Logger logger = Logger.getLogger(WatchChild.class);

    //zookeeper的连接地址
    private final static String CONNECTSTRING = "192.168.1.126";

    private final static String PATH = "/node1";
    //超时时间
    private final static int SESSION_TIMEOUT = 5000 * 1000;

    private ZooKeeper zk = null;

    public ZooKeeper startZK() throws IOException {
        return new ZooKeeper(CONNECTSTRING, SESSION_TIMEOUT, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                /*
                * 因为EventType.NodeChildrenChanged这个方法是节点改变才会监控到，满足不到，所以第一步就是给个else，一开始
                * 就给他打印出注册的父节点的子节点，下次有改动的话，就满足if条件，实现监控（监控的是一开始给的值，也就是
                * else中的传参）
                */
                if (watchedEvent.getType() == EventType.NodeChildrenChanged && watchedEvent.getPath().equals(PATH)) {
                    showChildNode(PATH);
                } else {
                    //注册父节点并打印初始节点有多少个
                    showChildNode(PATH);
                }
            }
        });
    }

    public void showChildNode(String nodePath) {
        List<String> list = null;
        try {
            //得到nodePath的子节点，要监控true
            list = zk.getChildren(nodePath, true);
            logger.info("*****************" + list);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 1 监控节点，完成注册
     * 2 按照注册的父节点，监控下面的子节点变化（增，删）
     *
     * @param args
     * @throws IOException
     * @throws InterruptedException
     * @throws KeeperException
     */
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        WatchChild watchOne = new WatchChild();

        watchOne.setZk(watchOne.startZK());
        Thread.sleep(Long.MAX_VALUE);


    }


    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }
}
