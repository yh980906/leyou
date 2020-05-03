package com.leyou.item.service;

import com.leyou.common.domain.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.domain.Sku;
import com.leyou.item.domain.Spu;
import com.leyou.item.domain.SpuDetail;

import java.util.List;

/**
 * 这个是完成商品列表的service
 */
public interface IGoodsService {


    PageResult<SpuBo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows);

    void saveGoods(SpuBo spuBo);

    SpuDetail querySpuDetailBySpuId(Long spuId);

    List<Sku> querySkusBySpuId(Long spuId);

    void updateGoods(SpuBo spuBo);

    Spu querySpuById(Long id);

    Sku querySkuById(Long id);
}
