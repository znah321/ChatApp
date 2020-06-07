package ChatApp;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

/**
 * 用户发送信息类
 * 功能：
 *     1、私聊---指令：/msg
 *     2、发送全体消息---指令：/broadcast
 *     3、离开聊天室---指令：/leave
 */
public class UserSend implements Runnable{
    private User user;
    //private byte[] buf;//待发送的数据
    //private int sendToPort;//发送端口

    //构造方法
    public UserSend(User user) {
        this.user = user;
    }

    //向服务器发送上线消息
    private synchronized void userLaunched(){
        //建立UDPSocket服务
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket();
            String onlineMessage = "@Server：" + this.user.getName() + "进入了聊天室";

            //数据打包
            byte[] bytes = onlineMessage.getBytes();
            DatagramPacket dp = new DatagramPacket(bytes, bytes.length, InetAddress.getByName("localhost"), 23333);

            //发送
            ds.send(dp);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ds.close();
        }
    }

    @Override
    public void run() {
        this.userLaunched();
        /**
         * 步骤：
         *     1、建立UDPSocket服务
         *     2、选择聊天功能（私聊or广播）
         *     3、建立数据包，并把聊天内容存入数据包中
         *     4、发送
         *     5、关闭Socket服务
         */
        Scanner s = new Scanner(System.in);
        String command = null;
        DatagramSocket ds = null;
        DatagramPacket dp;
        int msgPort = 0;

        while ((command = s.nextLine())!= "/leave"){
            try {
                //如果command为"/msg"，则为私聊，需指定私聊人名称
                if (command.equals("/msg")) {
                    //输入私聊人名称
                    System.out.println("---请输入私聊人名称：");
                    String msgName = null;
                    Scanner msgS = new Scanner(System.in);
                    msgName = msgS.nextLine();

                    //获取私聊人端口
                    msgPort = Server.userList.get(msgName).getPort();
                    if (msgPort != 0) {
                        //输入聊天内容
                        System.out.println("---请输入私聊内容：");
                        String message = null;
                        Scanner messageS = new Scanner(System.in);
                        message = "---" + user.getName() + "悄悄的对你说：" + messageS.nextLine();

                        //建立UDPSocket服务，数据打包
                        ds = new DatagramSocket();

                        byte[] buf = message.getBytes();
                        dp = new DatagramPacket(buf, buf.length, InetAddress.getByName("localhost"), msgPort);

                        //发送
                        ds.send(dp);
                        System.out.println("@Server：发送成功");
                    }
                    else{
                        System.out.println("@Server：未找到该用户！请检查输入是否正确！");
                    }
                }
                //如果command为"/broadcast"，则为广播
                else if (command.equals("/broadcast")) {
                    //输入聊天内容
                    System.out.println("---请输入广播内容：");
                    String message = null;
                    Scanner messageS = new Scanner(System.in);
                    message = user.getName() + "@全体成员：" + messageS.nextLine();

                    //建立UDPSocket服务，数据打包
                    ds = new DatagramSocket();

                    byte[] buf = message.getBytes();
                    dp = new DatagramPacket(buf, buf.length, InetAddress.getByName("localhost"), 23333);

                    //发送
                    ds.send(dp);
                    System.out.println("@Server：发送成功");
                }
                //其他情况，提示输入指令错误
                else{
                    System.out.println("@Server：指令错误！请重新输入！");
                }
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //并关闭UDPSocket服务
                ds.close();
            }
        }
    }
}
