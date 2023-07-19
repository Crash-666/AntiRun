package crash;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;

public class FileUtil
{
    public static int writeStringToTxt(String targetTxt, String str) {
        File file = new File(targetTxt);
        BufferedWriter bwriter;
        try {


            bwriter = new BufferedWriter(( new OutputStreamWriter(new FileOutputStream(file), "UTF-8")));
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

