package cn.edu.seu.alumni_background.controller;

import cn.edu.seu.alumni_background.aspect.WebResponseController;
import cn.edu.seu.alumni_background.config.interceptor.TokenRequired;
import cn.edu.seu.alumni_background.error.ServiceException;
import cn.edu.seu.alumni_background.log.annotation.LogController;
import cn.edu.seu.alumni_background.model.dao.entity.Banner;
import cn.edu.seu.alumni_background.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@TokenRequired
@LogController
@WebResponseController
@RestController
@RequestMapping("/banner")
public class BannerController {

    private final BannerService bannerService;

    @Autowired
    public BannerController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    @GetMapping("/list")
    public Object bannerList() {
        return bannerService.getAllBannersList();
    }

    @PostMapping("/post")
    public Object postBanner(
            @RequestBody Banner newBanner
    ) throws ServiceException {
        bannerService.changeBanner(newBanner);
        return "Banner修改成功！";
    }

    @PostMapping("/add")
    public Object addBanner(
        @RequestBody Banner newBanner
    ) throws ServiceException {
        bannerService.addBanner(newBanner);
        return "Banner添加成功！";
    }

    @DeleteMapping("/delete")
    public Object deleteBanner(@RequestParam Long bannerId) throws ServiceException {
        bannerService.deleteBannerByBannerId(bannerId);
        return "Banner删除成功！";
    }
}
