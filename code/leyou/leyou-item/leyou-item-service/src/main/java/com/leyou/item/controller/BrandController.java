package com.leyou.item.controller;

import com.leyou.common.domain.PageResult;
import com.leyou.item.domain.Brand;
import com.leyou.item.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
    /*
    @RequestMapping可以指定GET、POST请求方式
    @GetMapping等价于@RequestMapping的GET请求方式
    @RequestParam有三个属性，分别如下：
​       (1) value 请求参数的参数名,作为参数映射名称；
​       (2) required 该参数是否必填，默认为true(必填)，当设置成必填时，如果没有传入参数，报错；
​       (3) defaultValue 设置请求参数的默认值；
    */

/**
 * 品牌表
 * 对应tb_brand
 */
@Controller
@RequestMapping("/brand")
public class BrandController {
    @Autowired
    private IBrandService brandService;

    /**
     * 根据查询条件分页排序查询品牌信息
     * @param key 搜索的条件
     * @param page 当前页码
     * @param rows 每页展示的条数
     * @param sortBy 排序字段
     * @param desc 是否降序
     * @return
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandsByPage(
                                            @RequestParam(value = "key" ,required = false) String key,
                                            @RequestParam(value = "page" ,defaultValue = "1") Integer page,
                                            @RequestParam(value = "rows" ,defaultValue = "5") Integer rows,
                                            @RequestParam(value = "sortBy" ,required = false) String sortBy,
                                            @RequestParam(value = "desc" ,required = false) Boolean desc
                                            ){
        PageResult<Brand> pageResult = this.brandService.queryBrandsByPage(key,page,rows,sortBy,desc);
        if(CollectionUtils.isEmpty(pageResult.getItems()) ){
            //404 资源服务器未找到
            //return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(pageResult);
    }

    /**
     * 品牌新增功能
     * @param brand
     * @param cids
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids")List<Long> cids){
        brandService.saveBrand(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    /**
     * 根据品牌的ID删除品牌
     * @param bid
     */
    @DeleteMapping
    public ResponseEntity<Void> deletedBrand(@RequestParam("id")Long bid){
        this.brandService.deletedBrand(bid);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 修改品牌信息
     * 品牌的回显方法在categoryController中
     * @param
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateBrand(Brand brand, @RequestParam("cids")List<Long> cids){
        this.brandService.updateBrand(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据分类cid查询品牌
     * @param cid
     * @return
     */
    @GetMapping("/cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandsByCid(@PathVariable("cid")Long cid){

        List<Brand> list =  this.brandService.queryBrandsByCid(cid);
        if (CollectionUtils.isEmpty(list)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(list);
    }

    /**
     * 根据品牌ID查询brand
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandsById(@PathVariable("id")Long id){

        Brand brand =  this.brandService.queryBrandsById(id);
        if (brand==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brand);
    }

}
