package crash;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Assign extends Thread {
    private Socket socket;

    public Assign(Socket socket) {
        this.socket = socket;
    }

    public void run() {

        DataInputStream dis=null;


        try {//这是服务器端代码

            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            String encrypt = dis.readUTF();
            if(encrypt==null||encrypt.equals("")){
                return;
            }
            String content = Encrypt.decrypt(encrypt);
            Output.print(content);
            if(content.equals("UploadUserInfo"))
                new UploadUserInfo(socket,dis).start();
            else if(content.equals("GetDex"))
                new SendDex(socket,dis).start();
            else if(content.equals("GetConfig"))
                new SendConfig(socket).start();
            else
                System.out.println("未知功能");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
