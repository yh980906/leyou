package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.domain.PageResult;
import com.leyou.item.domain.Brand;
import com.leyou.item.mapper.IBrandMapper;
import com.leyou.item.service.IBrandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 品牌表的service接口实现类
 */
@Service
public class BrandServiceImpl implements IBrandService {

    @Autowired
    private IBrandMapper brandMapper;

    /**
     *根据查询条件分页排序查询品牌信息
     * @param key 搜索的条件
     * @param page 当前页码
     * @param rows 每页展示的条数
     * @param sortBy 排序字段
     * @param desc 是否降序
     * @return
     */
    @Override
    public PageResult<Brand> queryBrandsByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {

        //1.根据搜索的条件进行查询(使用模糊查询)，如果没有条件，则为查询全部

        //1.1 创建对象
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        //1.2 如果不为空，则说明输入了查询条件(可能按Name查询，也可能以首字母查询)，使用模糊查询
        if(StringUtils.isNotBlank(key)){
            criteria.andLike("name", "%" + key + "%").orEqualTo("letter", key);
        }
        //1.3 分页条件
        PageHelper.startPage(page, rows);
        //1.4 排序条件  如果排序字段不为空，则按desc的值排序该字段，否则默认
        if(StringUtils.isNotBlank(sortBy)){
            example.setOrderByClause(sortBy+" "+(desc ? "desc" : "asc"));
        }

        //2. 将查询出来的数据通过page和rows 使用pageHelper进行分页封装

        //2.1 查询的数据
        List<Brand> brands = this.brandMapper.selectByExample(example);
        //2.2 封装
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);
        //3.将数据封装为PageResult返回
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }

    /**
     * 品牌新增
     * @param brand
     * @param cids
     */
    @Transactional
    @Override
    public void saveBrand(Brand brand, List<Long> cids) {
        //新增品牌
        this.brandMapper.insertSelective(brand);
        //新增中间表
        cids.forEach(cid ->{
            this.brandMapper.insertCategoryAndBrand(cid, brand.getId());
        });
    }

    /**
     * 根据品牌的ID删除品牌
     * @param bid
     */
    @Transactional
    @Override
    public void deletedBrand(Long bid) {
        //品牌表删除
        this.brandMapper.deleteByPrimaryKey(bid);
        //品牌与商品分类中间表
        this.brandMapper.deleteCategoryAndBrandByBid(bid);
    }

    /**
     * 品牌的修改
     * @param brand
     * @param cids
     */
    @Transactional
    @Override
    public void updateBrand(Brand brand, List<Long> cids) {
        //品牌修改
        this.brandMapper.updateByPrimaryKeySelective(brand);
        //中间表修改
        cids.forEach(cid ->{
            this.brandMapper.updateCategoryAndBrand(cid, brand.getId());
        });
    }

    @Override
    public List<Brand> queryBrandsByCid(Long cid) {
        return this.brandMapper.selectBrandByCid(cid);
    }

    @Override
    public Brand queryBrandsById(Long id) {
        return this.brandMapper.selectByPrimaryKey(id);
    }


}
