package com.leyou.item.service;

import com.leyou.common.domain.PageResult;
import com.leyou.item.domain.Brand;

import java.util.List;

public interface IBrandService {


    PageResult<Brand> queryBrandsByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc);

    void saveBrand(Brand brand, List<Long> cids);

    void deletedBrand(Long bid);


    void updateBrand(Brand brand, List<Long> cids);

    List<Brand> queryBrandsByCid(Long cid);

    Brand queryBrandsById(Long id);
}
