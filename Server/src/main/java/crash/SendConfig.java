package crash;
/*  Author:Crash
    Date:2023/7/19
*/

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;
import java.io.BufferedOutputStream;

public class SendConfig extends Thread {
    private Socket socket;
    private static Object lock = new Object();

    public SendConfig(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        //3.获取输入流并获取客户信息
        DataOutputStream dos= null;
        DataInputStream dis=null;


        synchronized (lock) {
            try {//这是服务器端代码
                dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                String data = Encrypt.decrypt(dis.readUTF());
                Output.print(data);
                //     Output.print("1");
                String category_ = new JSONObject(data).getString("category");
                String time = new JSONObject(data).getString("time");
                //   Output.print("2");
                File jsonPath = new File(Data.FUNCTION_PATH + "/" + category_ + ".json");

                JSONObject outPutConfig = new JSONObject();
                //     Output.print("3");
                if (jsonPath.exists()) {
                    File[] files = new File(Data.FUNCTION_PATH).listFiles();
                    Boolean isContain = false;

                    for (File file:files) {

                        Output.print("存在");
                        String currentConfig = FileUtil.readStringFromtxt(file.toString());
                        if (new JSONObject(currentConfig).getString("UserTime").equals(time)) {
                            outPutConfig.put("config", currentConfig);
                            outPutConfig.put("category", category_);
                            isContain = true;
                            break;
                        }

                    }
                    if (!isContain) {
                        Output.print("存在，不同，创建");
                        String template = FileUtil.readStringFromtxt(Data.TEMPLATE_FUNCTION_PATH);
                        JSONObject newConfig = new JSONObject(template);
                        newConfig.put("UserTime", new JSONObject(data).get("time"));
                        outPutConfig.put("config", newConfig.toString());
                        String category = category_ + "_" + (files.length);
                        outPutConfig.put("category", category);
                        String path = Data.FUNCTION_PATH + "/" + category + ".json";
                        FileUtil.writeStringToTxt(path, newConfig.toString());
                    }
                } else {
                    Output.print("不存在，创建");
                    String template = FileUtil.readStringFromtxt(Data.TEMPLATE_FUNCTION_PATH);
                    JSONObject newConfig = new JSONObject(template);
                    newConfig.put("UserTime", new JSONObject(data).get("time"));
                    outPutConfig.put("config", newConfig.toString());
                    outPutConfig.put("category", category_);
                    FileUtil.writeStringToTxt(jsonPath.toString(), newConfig.toString());
                }
                dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                dos.writeUTF(Encrypt.encrypt(outPutConfig.toString()));
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
}
