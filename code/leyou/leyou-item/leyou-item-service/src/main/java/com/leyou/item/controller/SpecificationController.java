package com.leyou.item.controller;

import com.leyou.item.domain.SpecGroup;
import com.leyou.item.domain.SpecParam;
import com.leyou.item.service.ISpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 *  商品规格参数分组与规格参数组下的参数名的controller
 *  对应tb_spec_param和tb_spec_group两张表
 */
@Controller
@RequestMapping("/spec")
public class SpecificationController {

    @Autowired
    private ISpecificationService specificationService;


    /**
     * 查询该cid分类下的规格参数分组
     * @param cid
     * @return
     */
    @GetMapping("/groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupById(@PathVariable("cid")Long cid){
        List<SpecGroup> list = this.specificationService.queryGroupById(cid);
        if(CollectionUtils.isEmpty(list)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(list);
    }

    /**
     * 根据该规格参数分组的id查询(tb_spec_param)表中所对应的规格参数
     * @return
     */
    @GetMapping("/params")
    public ResponseEntity<List<SpecParam>> queryParams(@RequestParam(value = "gid",required = false)Long gid,
                                                           @RequestParam(value = "cid",required = false)Long cid,
                                                           @RequestParam(value = "generic",required = false)Boolean generic,
                                                           @RequestParam(value = "searching",required = false)Boolean searching
                                                           ){
        List<SpecParam> list = this.specificationService.queryParams(gid,cid,generic,searching);
        if(CollectionUtils.isEmpty(list)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(list);

    }

    /**
     * 添加参数分组
     * @param specGroup
     * @return
     */
    @PostMapping("/group")
    public ResponseEntity<Void> saveGroup(@RequestBody SpecGroup specGroup){
            this.specificationService.saveGroup(specGroup);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 删除参数分组
     * @param cid
     * @return
     */
    @DeleteMapping("/group/{cid}")
    public ResponseEntity<Void> delete(@PathVariable("cid")Long cid){
        this.specificationService.deleteGroup(cid);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/group")
    public ResponseEntity<Void> updateGroup(@RequestBody SpecGroup specGroup){
        this.specificationService.updateGroup(specGroup);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 通过cid 查询所有规格参数信息
     * @param cid
     * @return
     */
    @GetMapping("group/param/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupsWithParam(@PathVariable("cid") Long cid){
        List<SpecGroup> list = this.specificationService.queryGroupsWithParam(cid);
        if(CollectionUtils.isEmpty(list)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(list);
    }
}
