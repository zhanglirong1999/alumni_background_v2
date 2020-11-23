package cn.edu.seu.alumni_background.component.cos;

import cn.edu.seu.alumni_background.config.qcloud.QCloudCOSConfigurer;
import cn.edu.seu.alumni_background.error.ServiceException;
import cn.edu.seu.alumni_background.util.FileUtil;
import cn.edu.seu.alumni_background.util.LocalUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

@Component
public class COSManagerImpl implements COSManager {

    @Autowired
    private QCloudCOSConfigurer.QCloudHolder qCloudHolder;

    @Override
    public String buildNewFileNameWithType(
        MultipartFile multipartFile,
        String newFileNameWithoutType
    ) {
        return newFileNameWithoutType + Objects.requireNonNull(
            multipartFile.getOriginalFilename()
        ).substring(
            multipartFile.getOriginalFilename().lastIndexOf(".")
        );
    }

    @Override
    public File convertMultipartFileToFile(
        MultipartFile multipartFile,
        String newName
    ) throws IOException {
        // 获取到文件源路径
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null) {
            throw new IOException("原文件名为空!");
        }
        // 配置结果.
        File ansFile = new File(newName == null ? originalFilename : newName);
        // 使用 commons-io
        FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), ansFile);
        return ansFile;
    }

    @Override
    public String uploadFile(
        File file, String... subDirs
    ) throws ServiceException {
        // 首先获取到
        String ansFileUrl = getBaseUrl();
        // 创建新的文件路径
        StringBuilder bucketNameBuilder = new StringBuilder();
        for (String subDir : subDirs) {
            bucketNameBuilder.append("/").append(subDir);
        }
        String uploadFileKey =
            bucketNameBuilder.append("/").append(
                FileUtil.getFileName(file)
            ).toString();
        // 上传文件
        // 创建客户端
        COSClient cosClient = qCloudHolder.newCOSClient();
        // 上传图片文件到指定的桶中
        PutObjectRequest putObjectRequest = new PutObjectRequest(
            qCloudHolder.getBucketName(),
            uploadFileKey,
            file
        );
        cosClient.putObject(putObjectRequest);
        // 关闭
        qCloudHolder.closeCOSClient(cosClient);
        return ansFileUrl + putObjectRequest.getKey();
    }

    @Override
    public String uploadAndDeleteFile(
        File file,
        String... subDirs
    ) throws ServiceException {
        String ans = uploadFile(file, subDirs);
        LocalUtil.deleteFileUnderProjectDir(
            file.getName()
        );
        return ans;
    }

    @Override
    public String getBaseUrl() {
        return qCloudHolder.getBaseUrl();
    }

    @Override
    public Boolean hasObject(String objectKey) {
        Boolean ans = null;
        // 创建客户端
        COSClient cosClient = qCloudHolder.newCOSClient();
        // 尝试获取元数据
        try {
            ObjectMetadata objectMetadata = cosClient.getObjectMetadata(
                qCloudHolder.getBucketName(), objectKey
            );
            ans = objectMetadata != null;
        } catch (Exception any) {
            ans = false;
        } finally {
            if (cosClient != null) {
                qCloudHolder.closeCOSClient(cosClient);
            }
        }
        return ans;
    }

    @Override
    public void deleteObject(String objectKey) throws IOException {
        COSClient cosClient = qCloudHolder.newCOSClient();
        if (cosClient != null) {
            cosClient.deleteObject(qCloudHolder.getBucketName(), objectKey);
            qCloudHolder.closeCOSClient(cosClient);
        } else {
            throw new IOException("创建客户端失败.");
        }
    }

    @Override
    public String saveAccountAvatar(
        String oAvatar,
        String newFileName,
        String... subDirs
    ) throws IOException, ServiceException {
        // 获取 inputStream
        URL url = new URL(oAvatar);
        HttpsURLConnection httpsURLConnection =
            (HttpsURLConnection) url.openConnection();
        InputStream in = httpsURLConnection.getInputStream();
        // 转成 file 上传
        File file = new File(newFileName);
        FileUtils.copyInputStreamToFile(in, file);
        return uploadAndDeleteFile(file, subDirs);
    }

    @Override
    public Set<String> getAllKeys() {
        Set<String> keys = new HashSet<>();
        ListObjectsRequest listObjectsRequest =
            new ListObjectsRequest();
        listObjectsRequest.setBucketName(qCloudHolder.getBucketName());
        // 设置为空获取所有对象
        listObjectsRequest.setDelimiter("");
        listObjectsRequest.setMaxKeys(1000);
        // 创建对象
        COSClient cosClient = qCloudHolder.newCOSClient();
        ObjectListing objectListing = null;
        do {
            objectListing = cosClient.listObjects(listObjectsRequest);
            List<COSObjectSummary> cosObjectSummaries =
                objectListing.getObjectSummaries();
            for (COSObjectSummary s: cosObjectSummaries) {
                keys.add(s.getKey());
            }
            String nextMarker = objectListing.getNextMarker();
            listObjectsRequest.setMarker(nextMarker);
        } while (objectListing.isTruncated());
        qCloudHolder.closeCOSClient(cosClient);
        return keys;
    }
}
