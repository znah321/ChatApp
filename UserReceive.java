package ChatApp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * 用户接受信息类
 * 功能：
 *     接受数据包，并返回给用户其中的信息
 */
public class UserReceive implements Runnable{
    private User user;

    //构造方法
    public UserReceive(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        /**思路：
         *     1、建立UDPSocket服务，监听发送至用户所在端口的数据包
         *     2、接受数据包，并返回给用户其中的信息
         */
        DatagramSocket dsReceive = null;
        DatagramPacket dp;
        while (true){
            try {
                //建立UDPSocket服务，创建用于接收数据的数据包
                dsReceive = new DatagramSocket(user.getPort());
                byte[] buf = new byte[1025 * 64];
                dp = new DatagramPacket(buf, buf.length);

                //接收数据包
                dsReceive.receive(dp);

                //返回给用户其中的信息
                String data = new String(dp.getData(), 0, dp.getLength());
                System.out.println(data);

            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                dsReceive.close();
            }
        }
    }
}
