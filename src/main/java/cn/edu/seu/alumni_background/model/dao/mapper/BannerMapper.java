package cn.edu.seu.alumni_background.model.dao.mapper;

import cn.edu.seu.alumni_background.model.dao.entity.Banner;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
@Repository
public interface BannerMapper extends Mapper<Banner> {
    void update(Banner newBanner);
    Banner getValidBanner(Long bannerId);
    void insertWithoutId(Banner newBanner);
    List<Banner> selectAllValidBanners();
    void deleteById(Long bannerId);
}