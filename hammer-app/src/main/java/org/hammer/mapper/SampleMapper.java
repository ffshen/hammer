package org.hammer.mapper;
 

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select; 
import org.hammer.domain.SampleDomain;
import org.hammer.vo.Pageable;

/**
 * @author shenx
 * @date 2017年2月3日
 * @see
 * @version
 */

@Mapper
public interface SampleMapper {

    @Select("select * from odc_i_order where order_id = #{orderId}")
    public SampleDomain selectByPk(@Param("orderId") Integer orderId) ;
    
    @Select("select * from odc_i_order ")
    public List<SampleDomain> selectByPkPage(Pageable page) ;
}
