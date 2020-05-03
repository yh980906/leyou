package com.leyou.item.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 商品分类API
 */
@RequestMapping("/category")
public interface CategoryApi {

    /**
     * 根据商品分类的ID查询分类名称
     * @param ids
     * @return
     */
    @GetMapping("names")
    public List<String> queryNamesByIds(@RequestParam("ids")List<Long> ids);
}
