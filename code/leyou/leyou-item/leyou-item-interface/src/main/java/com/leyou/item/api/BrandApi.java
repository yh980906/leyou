package com.leyou.item.api;


import com.leyou.item.domain.Brand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
    /*
    @RequestMapping可以指定GET、POST请求方式
    @GetMapping等价于@RequestMapping的GET请求方式
    @RequestParam有三个属性，分别如下：
​       (1) value 请求参数的参数名,作为参数映射名称；
​       (2) required 该参数是否必填，默认为true(必填)，当设置成必填时，如果没有传入参数，报错；
​       (3) defaultValue 设置请求参数的默认值；
    */

/**
 * 品牌
 *
 */
@RequestMapping("/brand")
public interface BrandApi {

    @GetMapping("{id}")
    public Brand queryBrandsById(@PathVariable("id")Long id);

}
