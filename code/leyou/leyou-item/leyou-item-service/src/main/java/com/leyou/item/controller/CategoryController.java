package com.leyou.item.controller;

import com.leyou.item.domain.Category;
import com.leyou.item.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 商品类目表的controller
 * 对应tb_category表
 */
@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;
    /*
    @RequestMapping可以指定GET、POST请求方式
    @GetMapping等价于@RequestMapping的GET请求方式
    @RequestParam有三个属性，分别如下：
​       (1) value 请求参数的参数名,作为参数映射名称；
​       (2) required 该参数是否必填，默认为true(必填)，当设置成必填时，如果没有传入参数，报错；
​       (3) defaultValue 设置请求参数的默认值；
    */

    /**
     * 根据父节点ID查询子节点id（查询分类信息）
     * @param pid
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Category>> queryCategoriesByPid(@RequestParam(value ="pid",defaultValue = "0")Long pid){
            if(pid==null || pid<0){
                //400 参数不合法
                //return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                //return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                return ResponseEntity.badRequest().build();
            }
            List<Category> categories = this.categoryService.queryCategoriesByPid(pid);
            //判断null应该在前 建议使用以下方法
            //if(categories==null || categories.size()<=0 )
            if(CollectionUtils.isEmpty(categories)){
                //404 资源服务器未找到
                //return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                return ResponseEntity.notFound().build();
            }
            //查询成功
            return ResponseEntity.ok(categories);
    }

    /**
     * 通过 @PathVariable 可以将 URL 中占位符参数绑定到控制器处理方法的入参中：
     *  URL 中的 {xxx} 占位符可以通过@PathVariable(“xxx“) 绑定到操作方法的入参中。
     * 通过修改的这个品牌的id查询查询其分类信息
     * 这个方法是为了在修改品牌数据时 回显他的信息
     * @param bid
     * @return
     */
    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> updateBrand(@PathVariable("bid") Long bid){
        List<Category> list = this.categoryService.queryCategoriesByBid(bid);
        if(CollectionUtils.isEmpty(list)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(list);
    }

    /**
     * 根据商品分类的ID查询分类名称
     * @param ids
     * @return
     */
    @GetMapping("names")
    public ResponseEntity<List<String>> queryNamesByIds(@RequestParam("ids")List<Long> ids){

        List<String> names = this.categoryService.queryNamesByIds(ids);
        if (CollectionUtils.isEmpty(names)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(names);
    }
}
