package com.leyou.item.api;

import com.leyou.item.domain.SpecGroup;
import com.leyou.item.domain.SpecParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 *
 *  商品规格参数分组与规格参数组下的参数名的controller
 *  对应tb_spec_param和tb_spec_group两张表
 */
@RequestMapping("/spec")
public interface SpecificationApi {


    /**
     * 根据该规格参数分组的id查询(tb_spec_param)表中所对应的规格参数
     * @return
     */
    @GetMapping("/params")
    public List<SpecParam> queryParams(@RequestParam(value = "gid",required = false)Long gid,
                                                           @RequestParam(value = "cid",required = false)Long cid,
                                                           @RequestParam(value = "generic",required = false)Boolean generic,
                                                           @RequestParam(value = "searching",required = false)Boolean searching
                                                           );

    /**
     * 通过cid 查询所有规格参数信息
     * @param cid
     * @return
     */
    @GetMapping("/group/param/{cid}")
    public List<SpecGroup> queryGroupsWithParam(@PathVariable("cid") Long cid);
}
