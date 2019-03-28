package cn.wujw.textboot.common;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Desc:
 *
 * @author wujw
 * @email jiwenwu@outlook.com
 * @date 2019-03-01
 */
public class IOUtils {
    private IOUtils(){

    }

    public static void writeToLocal(String destination, InputStream input)
            throws IOException {
        int index;
        byte[] bytes = new byte[1024];
        FileOutputStream downloadFile = new FileOutputStream(destination);
        while ((index = input.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
        }
        input.close();
        downloadFile.close();
    }

    /**
     * inputStream 转byte
     * @param inputStream
     * @return
     */
    public static byte[] inputStream2ByteArray(InputStream inputStream) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int length;
        try {
            while (-1 != (length = inputStream.read(buffer))) {
                output.write(buffer, 0, length);
            }
        }catch (Exception e){
            return new byte[0];
        }
        return output.toByteArray();
    }

}
