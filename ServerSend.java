package ChatApp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 服务器发送消息类：
 * 功能：
 *     1、当某用户上线时，向全体用户广播
 *     2、接受来自某用户发送全体消息的请求，并发送至其他用户
 */
public class ServerSend implements Runnable{
    //private static Server server;

    //setter方法
    //public static void setServer(Server server) {
    //    ServerSend.server = server;
    //}

    //发送全体消息
    public static void receiveMsg(){
        synchronized (ServerSend.class) {
            //Socket服务、数据包
            DatagramSocket serverSocket = null;
            DatagramPacket serverPacket;
            try {
                //获取发送至数据包中的信息
                serverSocket = new DatagramSocket(23333);
                byte[] buf = new byte[1024 * 64];
                serverPacket = new DatagramPacket(buf, buf.length);
                serverSocket.receive(serverPacket);
                String message = new String(serverPacket.getData(), 0, serverPacket.getLength());

                //发送消息给全体用户，创建临时UDPSocket服务
                Set<Map.Entry<String, User>> KeySet = Server.userList.entrySet();
                Iterator<Map.Entry<String, User>> it = KeySet.iterator();
                DatagramSocket tempSocket = new DatagramSocket();
                while (it.hasNext()) {
                    //数据重新打包
                    byte[] bytes = message.getBytes();
                    DatagramPacket tempPacket = new DatagramPacket(bytes, bytes.length, InetAddress.getByName("localhost"), it.next().getValue().getPort());

                    //发送
                    tempSocket.send(tempPacket);
                }
                //关闭临时UDPSocket服务
                tempSocket.close();

            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                serverSocket.close();
            }
        }
    }

    @Override
    public void run() {
        while (true){
            receiveMsg();
        }
    }
}
