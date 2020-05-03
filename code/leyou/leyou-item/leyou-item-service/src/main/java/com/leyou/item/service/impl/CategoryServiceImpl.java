package com.leyou.item.service.impl;

import com.leyou.item.domain.Category;
import com.leyou.item.mapper.ICategoryMapper;
import com.leyou.item.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类的service接口实现类
 */
@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private ICategoryMapper categoryMapper;

    /**
     * 根据父节点查询子节点
     * @param pid
     * @return
     */
    @Override
    public List<Category> queryCategoriesByPid(Long pid) {
        Category record = new Category();
        record.setParentId(pid);
        return this.categoryMapper.select(record);
    }

    /**
     * 通过修改的这个品牌的id查询查询其分类信息
     * 根据子节点查询父节点
     * @param bid
     * @return
     */
    @Override
    public List<Category> queryCategoriesByBid(Long bid) {
        return this.categoryMapper.queryCategoriesByBid(bid);
    }

    /**
     * 根据ids查询分类名称
     * @param ids
     * @return
     */
    public List<String> queryNamesByIds(List<Long> ids) {
        List<Category> list = this.categoryMapper.selectByIdList(ids);
        List<String> names = new ArrayList<>();
        for (Category category : list) {
            names.add(category.getName());
        }
        return names;
        // return list.stream().map(category -> category.getName()).collect(Collectors.toList());
    }
}
