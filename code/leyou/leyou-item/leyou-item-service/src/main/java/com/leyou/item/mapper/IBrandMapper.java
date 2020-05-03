package com.leyou.item.mapper;

import com.leyou.item.domain.Brand;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface IBrandMapper extends Mapper<Brand> {


    @Insert("insert into tb_category_brand(category_id,brand_id) VALUES(#{cid},#{bid})")
    void insertCategoryAndBrand(@Param("cid") Long cid, @Param("bid") Long bid);

    @Delete("delete from tb_category_brand where brand_id=#{bid}")
    void deleteCategoryAndBrandByBid(@Param("bid") Long bid);

    @Update("update tb_category_brand set category_id = #{cid} where brand_id = #{bid} ")
    void updateCategoryAndBrand(@Param("cid") Long cid,@Param("bid")  Long bid);

    @Select("SELECT * from tb_brand where id in(SELECT brand_id from tb_category_brand WHERE category_id = #{cid})")
    //@Select("SELECT b.* from tb_brand b INNER JOIN tb_category_brand cb on b.id=cb.brand_id where cb.category_id=#{cid}")
    List<Brand> selectBrandByCid(@Param("cid")Long cid);
}
