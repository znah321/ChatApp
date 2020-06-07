package ChatApp;

import java.io.IOException;
import java.net.*;

public class User_01 {
    private static User user_01 = new User(01, "张三", "localhost", 10001);

    public static void main(String[] args) {
        Server.userList.put("张三", user_01);
        Server.onlineUserCount++;
        System.out.println(Server.userList);
        //创建发送信息、接收信息线程
        UserSend userSend = new UserSend(User_01.user_01);
        UserReceive userReceive = new UserReceive(User_01.user_01);

        Thread t1 = new Thread(userSend);
        Thread t2 = new Thread(userReceive);

        //线程启动
        t1.start();
        t2.start();
    }
}
