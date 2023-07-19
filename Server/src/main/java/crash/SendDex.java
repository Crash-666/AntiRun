package crash;
/*  Author:Crash
    Date:2023/7/19
*/

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.io.BufferedInputStream;

public class SendDex extends Thread {
    private Socket socket;
    private DataInputStream dis=null;

    public SendDex(Socket socket,DataInputStream dis_) {
        this.socket = socket;
        this.dis = dis_;
    }

    public void run() {
        //3.获取输入流并获取客户信息
        DataOutputStream dos= null;


        synchronized (this) {
            try {//这是服务器端代码

                dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                FileInputStream fileInputStream=new FileInputStream(Data.LOADDEX_PATH);
                byte[] bytes=new byte[1024];
                int i;
                while ((i = fileInputStream.read(bytes)) != -1) {//看有没有
                    dos.write(bytes, 0, i);
                }
                fileInputStream.close();
                dos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //4.关闭相关资源
                try {
                    if (dos != null) dos.close();
                    if (dis != null) dis.close();
                    if (socket != null) socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static int writeStringToTxt(String targetTxt, String str) {
        File file = new File(targetTxt);
        BufferedWriter bwriter;
        try {
            bwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            bwriter.write(str);
            bwriter.flush();
            bwriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    public static String readStringFromtxt(String txtpath) {
        File file = new File(txtpath);
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            while ((s = br.readLine()) != null) {
                result.append(System.lineSeparator() + s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}

