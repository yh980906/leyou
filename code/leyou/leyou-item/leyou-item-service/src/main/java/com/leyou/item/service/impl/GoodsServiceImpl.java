package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.domain.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.domain.Sku;
import com.leyou.item.domain.Spu;
import com.leyou.item.domain.SpuDetail;
import com.leyou.item.domain.Stock;
import com.leyou.item.mapper.*;
import com.leyou.item.service.ICategoryService;
import com.leyou.item.service.IGoodsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class GoodsServiceImpl implements IGoodsService {

    @Autowired
    private ISpuDetailMapper spuDetailMapper;
    @Autowired
    private ISpuMapper spuMapper;
    @Autowired
    private IBrandMapper brandMapper;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private ISkuMapper skuMapper;
    @Autowired
    private IStockMapper stockMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;


    @Override
    public PageResult<SpuBo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows) {
        //1.添加条件
        //1.1 创建对象
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();

        //1.2判断搜索框是否有条件，有的话则进行模糊查询
        if(StringUtils.isNotBlank(key)){
            // 有条件
            criteria.andLike("title","%"+key+"%");

        }
        //1.3 判断是查询全部、上架还是下架
        if(saleable!=null){
            criteria.andEqualTo("saleable",saleable);
        }

        //2. 添加分页
        PageHelper.startPage(page,rows);
        //3. 进行spu查询,获取spu集合
        List<Spu> spus = this.spuMapper.selectByExample(example);

        PageInfo<Spu> pageInfo = new PageInfo<>(spus);

        //4. 将spu转换为spuBo，并添加上cname与bname
        List<SpuBo> spuBos = new ArrayList<>();
        //4.1 遍历sous
        spus.forEach(spu->{
            SpuBo spuBo = new SpuBo();
            //将spu(spus)中的值复制到spuBo中
            BeanUtils.copyProperties(spu,spuBo);
            //4.2查询商品分类cname
            List<String> names = this.categoryService.queryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            spuBo.setCname(StringUtils.join(names, "/"));
            //4.3查询商品品牌bname；
            spuBo.setBname(this.brandMapper.selectByPrimaryKey(spu.getBrandId()).getName());
            spuBos.add(spuBo);
        });

        return new PageResult<>(pageInfo.getTotal(), spuBos);

    }

    @Override
    @Transactional
    public void saveGoods(SpuBo spuBo) {
        // 新增spu
        // 设置默认字段
        spuBo.setId(null);
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(spuBo.getCreateTime());
        this.spuMapper.insertSelective(spuBo);

        //新增spuDetail
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spuBo.getId());
        this.spuDetailMapper.insertSelective(spuDetail);

        this.saveSkuAndStock(spuBo);

        this.sendMessage(spuBo.getId(),"insert");
    }


    private void saveSkuAndStock(SpuBo spuBo) {
        //新增sku
        List<Sku> skus = spuBo.getSkus();

        skus.forEach(sku->{
            // 新增sku
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.skuMapper.insertSelective(sku);
            //新增库存
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insertSelective(stock);
        });

    }

    /**
     * 回显修改操作中的SpuDetail
     * @param spuId
     * @return
     */
    @Override
    public SpuDetail querySpuDetailBySpuId(Long spuId) {
        return this.spuDetailMapper.selectByPrimaryKey(spuId);
    }

    /**
     * 回显修改操作中的Sku
     * @param spuId
     * @return
     */
    @Override
    public List<Sku> querySkusBySpuId(Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skus = this.skuMapper.select(sku);
        skus.forEach(s->{
            Stock stock = this.stockMapper.selectByPrimaryKey(s.getId());
            s.setStock(stock.getStock());
        });
        return skus;
    }


    /**
     * 修改商品信息
     *
     * 分析： 因为sku表、与stock表都是可能有增加、删除或者修改的，所以需要删除然后添加
     *       而spu表、spuDetail 不可能删除或者添加，只是做了更改 ，所以不需要删除后添加
     *
     *      stock是以skuid为主键，所以先删除
     * @param spuBo
     */
    @Override
    @Transactional
    public void updateGoods(SpuBo spuBo) {

        //获取spuId
        Long spu_id = spuBo.getId();

        Sku sku = new Sku();
        sku.setSpuId(spu_id);
        List<Sku> skus = this.skuMapper.select(sku);

        skus.forEach(s -> {
        //1.应该先删除sku的库存表记录stock
            this.stockMapper.deleteByPrimaryKey(s.getId());
        });

             //2.删除sku表记录
            Sku sku1 = new Sku();
            sku1.setSpuId(spu_id);
            this.skuMapper.delete(sku1);

        //3.新增sku
        //4.新增stock
        saveSkuAndStock(spuBo);
        //5.修改spu表
        spuBo.setLastUpdateTime(new Date());
        spuBo.setCreateTime(null);
        spuBo.setValid(null);
        spuBo.setSaleable(null);
        this.spuMapper.updateByPrimaryKeySelective(spuBo);

        //6.修改spu_detail
        this.spuDetailMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());

        this.sendMessage(spuBo.getId(),"update");
    }

    /**
     * 根据spuId查询Spu
     * @param id
     * @return
     */
    @Override
    public Spu querySpuById(Long id) {
        Spu spu = this.spuMapper.selectByPrimaryKey(id);
        return spu;
    }

    /**
     * 根据SkuId 查询Sku
     * @param id
     * @return
     */
    @Override
    public Sku querySkuById(Long id) {

        return this.skuMapper.selectByPrimaryKey(id);
    }

    /**
     * 发送消息到mq的方法
     * 消息队列生产者抽取
     * @param id
     * @param type
     */
    private void sendMessage(Long id, String type){
        // 发送消息
        try {
            this.amqpTemplate.convertAndSend("item." + type, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
