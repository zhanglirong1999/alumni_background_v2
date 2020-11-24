package cn.edu.seu.alumni_background.service;

import cn.edu.seu.alumni_background.error.ServiceException;
import cn.edu.seu.alumni_background.model.dao.entity.Banner;
import cn.edu.seu.alumni_background.validation.NotEmpty;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface BannerService {
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    List<Banner> getAllBannersList();

    @Transactional(propagation = Propagation.REQUIRED)
    void addBanner(@NotEmpty Banner newBanner);

    @Transactional(propagation = Propagation.REQUIRED)
    void changeBanner(@NotEmpty Banner newBanner) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED)
    void deleteBannerByBannerId(@NotEmpty Long bannerId) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    boolean hasValidBanner(@NotEmpty Long bannerId);
}
