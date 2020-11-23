package cn.edu.seu.alumni_background.util;

import cn.edu.seu.alumni_background.error.ServiceException;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class FileUtil {

    public static String getFileName(File file) throws ServiceException {
        if (file == null) {
            throw new ServiceException("当前文件为空!");
        }
        String name = file.getName();
        if (name.equals("")) {
            throw new ServiceException("文件名不存在!");
        }
        return name.substring(name.lastIndexOf(File.separator) + 1);
    }

    public static boolean isSameAvatar(String avatar1, String avatar2)
        throws IOException {
        if (avatar1 == null || !avatar1.startsWith("https://")) {
            throw new IOException("头像路径非法");
        }
        if (avatar2 == null || !avatar2.startsWith("https://")) {
            throw new IOException("头像路径非法");
        }
        if (avatar1.equals(avatar2)) {
            return true;
        }
        InputStream in1 = getFromURL(avatar1),
            in2 = getFromURL(avatar2);
        byte[] bs1 = readBytes(in1), bs2 = readBytes(in2);
        return equalBytes(bs1, bs2);
    }

    public static InputStream getFromURL(String s) throws IOException {
        URL url1 = new URL(s);
        HttpsURLConnection conn1 = (HttpsURLConnection) url1.openConnection();
        return conn1.getInputStream();
    }

    public static boolean equalBytes(byte[] bs1, byte[] bs2) {
        if (bs1 == null) {
            return bs2 == null;
        } else {
            if (bs1.length != bs2.length) {
                return false;
            }
            for (int i = 0; i < bs1.length; ++i) {
                if (bs1[i] != bs2[i]) {
                    return false;
                }
            }
            return true;
        }
    }

    public static byte[] readBytes(InputStream in) throws IOException {
        byte[] temp = new byte[in.available()];
        byte[] result = new byte[0];
        int size = 0;
        while ((size = in.read(temp)) != -1) {
            byte[] readBytes = new byte[size];
            System.arraycopy(temp, 0, readBytes, 0, size);
            result = mergeArray(result, readBytes);
        }
        return result;
    }

    public static byte[] mergeArray(byte[]... a) {
        // 合并完之后数组的总长度
        int index = 0;
        int sum = 0;
        for (byte[] bytes : a) {
            sum = sum + bytes.length;
        }
        byte[] result = new byte[sum];
        for (byte[] bytes : a) {
            int lengthOne = bytes.length;
            if (lengthOne == 0) {
                continue;
            }
            // 拷贝数组
            System.arraycopy(bytes, 0, result, index, lengthOne);
            index = index + lengthOne;
        }
        return result;
    }
}
