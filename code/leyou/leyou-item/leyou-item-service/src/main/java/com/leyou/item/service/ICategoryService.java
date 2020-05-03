package com.leyou.item.service;

import com.leyou.item.domain.Category;

import java.util.List;

/**
 * 商品分类的service接口声明
 */
public interface ICategoryService {


    List<Category> queryCategoriesByPid(Long pid);

    List<Category>  queryCategoriesByBid(Long bid);

    List<String> queryNamesByIds(List<Long> asList);
}
