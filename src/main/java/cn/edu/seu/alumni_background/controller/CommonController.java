package cn.edu.seu.alumni_background.controller;

import cn.edu.seu.alumni_background.aspect.WebResponseController;
import cn.edu.seu.alumni_background.component.cos.COSManager;
import cn.edu.seu.alumni_background.config.interceptor.TokenRequired;
import cn.edu.seu.alumni_background.error.ServiceException;
import cn.edu.seu.alumni_background.log.annotation.LogController;
import cn.edu.seu.alumni_background.util.IDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@LogController
@TokenRequired
@WebResponseController
@RestController
public class CommonController {
    private COSManager cosManager;

    @Autowired
    void setCosManager(COSManager cosManager) {
        this.cosManager = cosManager;
    }

    @PostMapping("/uploadFile")
    public Object uploadFile(
            @RequestParam(name = "file") MultipartFile multipartFile
    ) throws IOException, ServiceException {
        String newNameWithoutType = String.valueOf(IDUtil.nextId());

        String newNameWithType = this.cosManager.buildNewFileNameWithType(
                multipartFile, newNameWithoutType
        );

        File file = this.cosManager.convertMultipartFileToFile(multipartFile, newNameWithType);

        return this.cosManager.uploadAndDeleteFile(file);
    }

    @DeleteMapping("/deleteFile")
    public Object deleteFile(
            @RequestParam String fileUrl
    ) throws ServiceException {
        try {
            cosManager.deleteObject(fileUrl.substring(fileUrl.lastIndexOf("/") + 1));
        } catch (IOException e) {
            throw new ServiceException("删除失败！");
        }
        return "删除成功！";
    }
}
