package ChatApp;

import java.io.IOException;
import java.net.*;

public class User_02 {
    private static User user_02 = new User(02, "李四", "localhost", 10002);

    public static void main(String[] args) {
        Server.userList.put("李四", user_02);
        Server.onlineUserCount++;
        System.out.println(Server.userList);
        //创建发送信息、接收信息线程
        UserSend userSend = new UserSend(User_02.user_02);
        UserReceive userReceive = new UserReceive(User_02.user_02);

        Thread t1 = new Thread(userSend);
        Thread t2 = new Thread(userReceive);

        //线程启动
        t1.start();
        t2.start();
    }
}
