package crash;
/*  Author:Crash
    Date:2023/7/19
*/
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

public class Encrypt {
    private static final String AES_ALGORITHM = "AES";

    // 加密
    public static String encrypt(String json) throws Exception {
        String key = "android";
        SecretKeySpec secretKeySpec = generateKey(key);
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(json.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // 解密
    public static String decrypt(String encryptedJson) throws Exception {
        String key = "android";
        SecretKeySpec secretKeySpec = generateKey(key);
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedJson);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }

    private static SecretKeySpec generateKey(String key) throws Exception {
        byte[] keyBytes = key.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        keyBytes = sha.digest(keyBytes);
        keyBytes = Arrays.copyOf(keyBytes, 16);
        return new SecretKeySpec(keyBytes, AES_ALGORITHM);
    }


}
