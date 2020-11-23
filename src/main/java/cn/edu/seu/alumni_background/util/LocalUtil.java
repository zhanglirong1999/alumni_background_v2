package cn.edu.seu.alumni_background.util;

import java.io.File;

public class LocalUtil {

    public static void deleteFileUnderProjectDir(String fileName) {
        // 然后应该删除项目目录下的本地文件
        File targetFile = new File(
            System.getProperty("user.dir") + File.separator + fileName);
        boolean delete = targetFile.delete();
    }
}
