package com.leyou.item.service;

import com.leyou.item.domain.SpecGroup;
import com.leyou.item.domain.SpecParam;

import java.util.List;

public interface ISpecificationService {

    List<SpecGroup> queryGroupById(Long cid);

    

    void saveGroup(SpecGroup specGroup);

    void deleteGroup(Long cid);

    void updateGroup(SpecGroup specGroup);

    List<SpecParam> queryParams(Long gid, Long cid, Boolean generic, Boolean searching);

    List<SpecGroup> queryGroupsWithParam(Long cid);
}
