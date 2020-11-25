package cn.edu.seu.alumni_background.service.impl;

import cn.edu.seu.alumni_background.error.ServiceException;
import cn.edu.seu.alumni_background.model.dao.entity.Banner;
import cn.edu.seu.alumni_background.model.dao.mapper.BannerMapper;
import cn.edu.seu.alumni_background.service.BannerService;
import cn.edu.seu.alumni_background.util.IDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Validated
@Service
public class BannerServiceImpl implements BannerService {

    private final BannerMapper bannerMapper;

    @Autowired
    public BannerServiceImpl(BannerMapper bannerMapper) {
        this.bannerMapper = bannerMapper;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Banner> getAllBannersList() {
        return bannerMapper.selectAllValidBanners();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addBanner(Banner newBanner) {
//        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        Long id = IDUtil.nextId();
//        newBanner.setBannerId(id);
//        System.out.println(id);
//        newBanner.setCTime(now);
//        newBanner.setUTime(now);
//        newBanner.setValidStatus(1);
        newBanner.setBannerId(IDUtil.nextId());
        bannerMapper.insertNewBanner(newBanner);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void changeBanner(Banner newBanner) throws ServiceException {
        if (!hasValidBanner(newBanner.getBannerId())) {
            throw new ServiceException("当前修改Banner id: " + newBanner.getBannerId() + "不存在！");
        }
        bannerMapper.update(newBanner);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteBannerByBannerId(Long bannerId) throws ServiceException {
        if (!hasValidBanner(bannerId)) {
            throw new ServiceException("删除Banner id: " + bannerId + "不存在！");
        }
        bannerMapper.deleteById(bannerId);
    }

    @Override
    public boolean hasValidBanner(Long bannerId) {
        Banner banner = bannerMapper.getValidBanner(bannerId);
        return banner != null;
    }
}
