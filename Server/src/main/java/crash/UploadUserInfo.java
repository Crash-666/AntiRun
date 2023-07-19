package crash;
/*  Author:Crash
    Date:2023/7/19
*/

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import org.json.JSONObject;
import org.json.JSONException;

public class UploadUserInfo extends Thread {
    private Socket socket;
    DataInputStream dis=null;
    private static Object lock = new Object();
    public UploadUserInfo(Socket socket, DataInputStream dis) {
        this.socket = socket;
        this.dis = dis;
    }

    public void run() {
        //3.获取输入流并获取客户信息
        DataOutputStream dos= null;
        synchronized (lock) {
            try {//这是服务器端代码

                // dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                String content = Encrypt.decrypt(dis.readUTF());
                JSONObject data = new JSONObject(content);
                final boolean TYPE_MSG = Integer.parseInt(data.getString("type")) == 0;
                final boolean TYPE_INFO = !TYPE_MSG;
                final String CATEGORY = data.getString("category").toString();

                File User_Directoty = new File(Data.USER_PATH + "/" + CATEGORY);
                if (!User_Directoty.exists()) User_Directoty.mkdirs();
                if (TYPE_MSG) {
                    output(data, new File(User_Directoty + "/Log.txt"));
                } else if (TYPE_INFO) {
                    output(data, new File(User_Directoty + "/Info.txt"));
                }
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

    void output(JSONObject jb, File f) {

        try {
            String Origin_Content = "";
            if(f.exists())
                Origin_Content = FileUtil.readStringFromtxt(f.toString());
            if(!Origin_Content.contains(jb.getString("msg").toString())){
                FileUtil.writeStringToTxt(f.toString(), Origin_Content + "\n" + jb.getString("msg"));

            }
        } catch (JSONException e) {}

    }

}
