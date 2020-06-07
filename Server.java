package ChatApp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.*;

/**
 * 服务器类
 *     属性：用户列表，在线人数
 *     功能：
 *         1、当某用户上线时，向全体用户广播
 *         2、接受来自某用户发送全体消息的请求，并发送至其他用户
 *         3、接受来自某用户私聊的请求，并返回指定端口
 *         4、当服务器接收到"/stop"指令时，聊天室关闭，并向用户发送消息、强制用户下线
 *     方法：
 *         private void initServer();//启动服务器--->启动聊天室
 *         private synchronized void receiveMsg(String name);//name代表来自发送请求的用户名称
 *
 */
public class Server {
    public static HashMap<String, User> userList = new HashMap<String, User>();//用户列表
    public static int onlineUserCount = 0;

    //启动服务器
    private static void initServer(){
        System.out.println("@Server：Loading...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("@Server：Now the server is running.");
    }

    public static void main(String[] args) {
        //初始化服务器
        Server.initServer();

        //启动ServerSend线程
        Server server = new Server();
        ServerSend serverSend = new ServerSend();
        new Thread(serverSend).start();



        //向所有用户广播：聊天室关闭，并强制用户下线
    }
}
