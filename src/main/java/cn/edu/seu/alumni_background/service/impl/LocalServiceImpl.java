package cn.edu.seu.alumni_background.service.impl;

import cn.edu.seu.alumni_background.component.cos.COSManager;
import cn.edu.seu.alumni_background.error.ServiceException;
import cn.edu.seu.alumni_background.model.dao.entity.Account;
import cn.edu.seu.alumni_background.model.dao.mapper.AccountMapper;
import cn.edu.seu.alumni_background.service.LocalService;
import cn.edu.seu.alumni_background.util.FileUtil;
import cn.edu.seu.alumni_background.util.LocalUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class LocalServiceImpl implements LocalService {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private COSManager cosManager;

    @Override
    public String uploadLocalFile(
        File file, String... subDirs
    ) throws ServiceException {
        String s = cosManager.uploadFile(file, subDirs);
        LocalUtil.deleteFileUnderProjectDir(FileUtil.getFileName(file));
        return s;
    }

    @Override
    public String uploadLocalFile(
        File file,
        String newNameWithoutType,
        String... subDirs
    ) throws IOException, ServiceException {
        String type =
            file.getName().substring(
                file.getName().lastIndexOf(".")
            );
        String newFileName = newNameWithoutType + type;
        File newFile = new File(newFileName);
        FileUtils.copyInputStreamToFile(
            new FileInputStream(file), newFile
        );
        return uploadLocalFile(newFile, subDirs);
    }

    @Override
    public void resizeImage(
        String src, String dest,
        int w, int h
    ) throws IOException {
        double wr = 0, hr = 0;
        File srcFile = new File(src);
        File destFile = new File(dest);

        BufferedImage bufImg = ImageIO.read(srcFile); //读取图片
        Image iTemp = bufImg.getScaledInstance(w, h, Image.SCALE_SMOOTH);  // 设置缩放目标图片模板

        wr = w * 1.0 / bufImg.getWidth();     //获取缩放比例
        hr = h * 1.0 / bufImg.getHeight();

        AffineTransformOp ato =
            new AffineTransformOp(
                AffineTransform.getScaleInstance(wr, hr),
                null
            );
        iTemp = ato.filter(bufImg, null);
        try {
            ImageIO.write((BufferedImage) iTemp, dest.substring(dest.lastIndexOf(".") + 1), destFile); //写入缩减后的图片
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void fixupInvalidAvatar() throws IOException {
        Example example = Example.builder(
            Account.class
        ).where(
            Sqls.custom()
                .andEqualTo("validStatus", true)
//                .andEqualTo("step1Finished", true)
                .andEqualTo("registered", true)
//                .andEqualTo("name", "韦权")
        ).build();
        List<Account> accounts = accountMapper.selectByExample(example);
        // 根据性别更换头像
        byte[] errorBytes =
            FileUtil.readBytes(FileUtil.getFromURL(ERROR_AVATAR));
        for (Account account : accounts) {
            String avatar = account.getAvatar();
            if (avatar != null) {
                try {
                    byte[] bytes = FileUtil.readBytes(FileUtil.getFromURL(avatar));
                    if (FileUtil.equalBytes(bytes, errorBytes)) {
                        Account t = new Account();
                        t.setAccountId(account.getAccountId());
                        if (account.getGender() == 1) {
                            t.setAvatar(DEFAULT_AVATAR_1);
                        } else if (account.getGender() == 2) {
                            t.setAvatar(DEFAULT_AVATAR_2);
                        }
                        accountMapper.updateByPrimaryKeySelective(t);
                    }
                } catch (IOException ignored) {

                }
            }
        }
    }

    @Override
    public void clearCOS(String[] except) throws IOException {
        assert except != null;
        // 首先获取所有的 avatar 路径
        Set<String> allAvatars = accountMapper.getAllAvatars();
        // 获取到所有的 cos 下的文件名
        allAvatars.addAll(Arrays.asList(except));
        Set<String> usingKeys = new HashSet<>();
        for (String a: allAvatars) {
            if (a != null && !a.equals("")) {
                usingKeys.add(a.substring(a.lastIndexOf("/") + 1));
            }
        }
        // 获取到需要删除的 keys
        Set<String> allKeys = cosManager.getAllKeys();
        allKeys.removeAll(usingKeys);
        // 所有剩下的都要删除
        for (String k : allKeys) {
            cosManager.deleteObject(k);
        }
    }

    @Override
    public void clearCOS() throws IOException {
        String[] except = new String[]{
            "https://alumni-circle-1257046110.cos.ap-beijing.myqcloud.com/23492426273792.png",
            "https://alumni-circle-1257046110.cos.ap-beijing.myqcloud.com/23619916611584.png",
            "https://alumni-circle-1257046110.cos.ap-beijing.myqcloud.com/23619966248960.png",
            "https://alumni-circle-1257046110.cos.ap-beijing.myqcloud.com/23658617664512.png",
            "https://alumni-circle-1257046110.cos.ap-beijing.myqcloud.com/23658671754240.png",
            "https://alumni-circle-1257046110.cos.ap-beijing.myqcloud.com/23658710472704.png"
        };
        clearCOS(except);
    }
}
