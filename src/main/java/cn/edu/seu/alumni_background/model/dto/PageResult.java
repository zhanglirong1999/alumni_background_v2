package cn.edu.seu.alumni_background.model.dto;

import cn.edu.seu.alumni_background.error.ServiceException;
import cn.edu.seu.alumni_background.model.dao.entity.Account;
import com.github.pagehelper.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult {
    private Long total = 1L;
    private Integer totalPage = 1;
    private List<Object> pageList = null;

    public <E> PageResult wrapForList(List<E> _pageList) throws ServiceException {
        if (_pageList instanceof Page) {
            return wrapForPage((Page<E>) _pageList);
        } else {
            throw new ServiceException("转换 PageResult 失败!");
        }
    }

    public <E> PageResult wrapForPage(Page<E> page) {
        total = page.getTotal();
        totalPage = page.getPages();
        pageList = (List<Object>) page;
        return this;
    }
}
