package cn.edu.seu.alumni_background.service;

import cn.edu.seu.alumni_background.error.ServiceException;

import java.io.File;
import java.io.IOException;

public interface LocalService {

    String ERROR_AVATAR =
        "https://clab-1257046110.cos.ap-beijing.myqcloud.com/invalid-avatar.png";

    String DEFAULT_AVATAR_2 =
        "https://clab-1257046110.cos.ap-beijing.myqcloud.com/default-avatar-0.jpg";

    String DEFAULT_AVATAR_1 =
        "https://clab-1257046110.cos.ap-beijing.myqcloud.com/default-avatar-1.jpg";

    String uploadLocalFile(File file, String... subDirs) throws ServiceException;

    String uploadLocalFile(
        File file, String newNameWithoutType, String... subDirs
    ) throws IOException, ServiceException;

    void resizeImage(String src, String dest, int w, int h) throws IOException;

    void fixupInvalidAvatar() throws IOException;

    void clearCOS(String[] except) throws IOException;

    void clearCOS() throws IOException;
}
