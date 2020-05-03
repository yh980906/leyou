package com.leyou.goods.service.impl;

import com.leyou.goods.client.BrandClient;
import com.leyou.goods.client.CategoryClient;
import com.leyou.goods.client.GoodsClient;
import com.leyou.goods.client.SpecificationClient;
import com.leyou.goods.service.IGoodsService;
import com.leyou.item.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GoodsServiceImpl implements IGoodsService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;


    @Override
    public Map<String, Object> loadModel(Long id) {
        Map<String, Object> map = new HashMap<>();

        //1. 根据id查询spu对象
        Spu spu = this.goodsClient.querySpuById(id);
        //2. 根据id查询detail对象
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(id);
        //3. 根据id查询skus
        List<Sku> skus = this.goodsClient.querySkusBySpuId(id);
        //4. 根据id查询分类
        List<Long> cids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
        List<String> names = this.categoryClient.queryNamesByIds(cids);
        List<Map<String,Object>> categories = new ArrayList<>();
        for (int i = 0; i < cids.size(); i++) {
            Map<String, Object> categoryMap = new HashMap<>();
            categoryMap.put("id", cids.get(i));
            categoryMap.put("name", names.get(i));
            categories.add(categoryMap);
        }

        //4. 根据id查询品牌
        Brand brand = this.brandClient.queryBrandsById(spu.getBrandId());
        //5. 获取规格参数分组
        List<SpecGroup> groups = this.specificationClient.queryGroupsWithParam(spu.getCid3());
        //6. 查询特殊规格参数
        List<SpecParam> specParams = this.specificationClient.queryParams(null, spu.getCid3(), false, null);
        Map<Long, String> paramMap = new HashMap<>();
        specParams.forEach(param -> {
            paramMap.put(param.getId(), param.getName());
        });
        // 封装spu
        map.put("spu", spu);
        // 封装spuDetail
        map.put("spuDetail", spuDetail);
        // 封装sku集合
        map.put("skus", skus);
        // 分类
        map.put("categories", categories);
        // 品牌
        map.put("brand", brand);
        // 规格参数组
        map.put("groups", groups);
        // 查询特殊规格参数
        map.put("paramMap", paramMap);
        return map;
    }
}
