package cn.edu.seu.alumni_background.component.cos;

import cn.edu.seu.alumni_background.error.ServiceException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface COSManager {

    // 构建新的名字
    String buildNewFileNameWithType(
        MultipartFile multipartFile,
        String newFileNameWithoutType
    );

    // 将输入的文件转换成一个标准的文件用以之后的上传
    File convertMultipartFileToFile(
        MultipartFile multipartFile,
        String newName
    ) throws IOException;

    String uploadFile(
        File file,
        String... subDirs
    ) throws ServiceException;

    String uploadAndDeleteFile(
        File file, String... subDirs
    ) throws ServiceException;

    String getBaseUrl();

    // 判断一个桶中是否又一个对象
    Boolean hasObject(String objectKey);

    // 删除一个对象
    void deleteObject(String objectKey) throws IOException;

    String saveAccountAvatar(
        String oAvatar,
        String newFileName,
        String... subDirs
    ) throws IOException, ServiceException;

    // 获取桶中所有对象
    Set<String> getAllKeys();
}
