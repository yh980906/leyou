package com.leyou.item.api;


import com.leyou.common.domain.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.domain.Sku;
import com.leyou.item.domain.Spu;
import com.leyou.item.domain.SpuDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 商品
 */

public interface GoodsApi {


    /**
     * 根据条件分页查询spu（商品列表）
     *
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("/spu/page")
    public PageResult<SpuBo> querySpuByPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows
    );


    /**
     * 回显修改操作中的spuDetail(通过spuId查询spuDetail)
     *
     * @param spuId
     * @return
     */
    @GetMapping("/spu/detail/{spuId}")
    public SpuDetail querySpuDetailBySpuId(@PathVariable("spuId") Long spuId);

    /**
     * 回显修改操作中的Sku(通过spuId查询sku)
     *
     * @param spuId
     * @return
     */
    @GetMapping("/sku/list")
    public List<Sku> querySkusBySpuId(@RequestParam(value = "id") Long spuId);

    /**
     * 根据spuId查询Spu
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public Spu querySpuById(@PathVariable("id") Long id);

    /**
     * 根据skuId查询Sku
     * @param id
     * @return
     */
    @GetMapping("sku/{id}")
    public Sku querySkuById(@PathVariable("id")Long id);
}
