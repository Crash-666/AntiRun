package crash;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static Object LogLock = new Object();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket=new ServerSocket(Data.PORT);

            System.out.println("服务器启动，等待客户端的连接");
            System.out.println("\u4F60\u597D"); // 输出"你好"

            File f1 = new File(Data.FUNCTION_PATH);
            File f2 = new File(Data.USER_PATH);

            if (!f1.exists())
                f1.mkdirs();
            if (!f2.exists())
                f2.mkdirs();
            while (true) {

                Socket socket = serverSocket.accept();

                Assign serverThread=new Assign(socket);

                serverThread.start();
                System.out.println("用户连接成功: " + socket.getInetAddress());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
