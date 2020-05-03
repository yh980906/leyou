package com.leyou.item.service.impl;

import com.leyou.item.domain.SpecGroup;
import com.leyou.item.domain.SpecParam;
import com.leyou.item.mapper.ISpecGroupMapper;
import com.leyou.item.mapper.ISpecParamMapper;
import com.leyou.item.service.ISpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 *  商品规格参数分组与规格参数组下的参数名
 *  对应tb_spec_param和tb_spec_group两张表
 */
@Service
public class SpecificationServiceImpl implements ISpecificationService {

    @Autowired
    private ISpecGroupMapper specGroupMapper;
    @Autowired
    private ISpecParamMapper specParamMapper;

    /**
     * 通过cid查询其分组信息
     *  cid不是specGroup这个的主键
     * @param cid
     * @return
     */
    @Override
    public List<SpecGroup> queryGroupById(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        return this.specGroupMapper.select(specGroup);
    }
    /**
     * 查询(tb_spec_param)表中所对应的规格参数
     * @return
     */
    @Override
    public List<SpecParam> queryParams(Long gid, Long cid, Boolean generic, Boolean searching) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setGeneric(generic);
        specParam.setSearching(searching);

        return this.specParamMapper.select(specParam);
    }

    /**
     * 通过cid 查询所有规格参数分组及信息
     * @param cid
     * @return
     */
    @Override
    public List<SpecGroup> queryGroupsWithParam(Long cid) {
        List<SpecGroup> list = this.queryGroupById(cid);
        list.forEach(l -> {
            List<SpecParam> specParams = this.queryParams(l.getId(), null, null, null);
            l.setParams(specParams);
        });
        return list;
    }


    /**
     * 保存规格参数分组
     * @param specGroup
     */
    @Override
    public void saveGroup(SpecGroup specGroup) {
        this.specGroupMapper.insert(specGroup);
    }

    /**
     * 删除参数分组
     * @param cid
     */
    @Transactional
    @Override
    public void deleteGroup(Long cid) {
        this.specGroupMapper.deleteByPrimaryKey(cid);
    }

    /**
     * 修改参数分组
     * @param specGroup
     */
    @Override
    public void updateGroup(SpecGroup specGroup) {
        this.specGroupMapper.updateByPrimaryKeySelective(specGroup);
    }


}
