package com.dtf.demo2.dao;

import com.dtf.demo2.po.Sc;
import com.dtf.demo2.po.ScExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ScMapper {

    /**
     * This method corresponds to the database table Sc.
     *
     * @param example ScExample
     * @return number of records
     */
    int countByExample(ScExample example);

    /**
     * This method corresponds to the database table Sc.
     *
     * @param example ScExample
     * @return number of records
     */
    int deleteByExample(ScExample example);

    /**
     * This method corresponds to the database table Sc.
     *
     * @param record Sc
     * @return number of records
     */
    int insert(Sc record);

    /**
     * This method corresponds to the database table Sc.
     *
     * @param record Sc
     * @return number of records
     */
    int insertSelective(Sc record);

    /**
     * This method corresponds to the database table Sc.
     *
     * @param example ScExample
     * @return number of records
     */
    List<Sc> selectByExample(ScExample example);

    /**
     * This method corresponds to the database table Sc.
     *
     * @param record Sc
     * @param example ScExample
     * @return number of records
     */
    int updateByExampleSelective(@Param("record") Sc record, @Param("example") ScExample example);

    /**
     * This method corresponds to the database table Sc.
     *
     * @param record Sc
     * @param example ScExample
     * @return number of records
     */
    int updateByExample(@Param("record") Sc record, @Param("example") ScExample example);

}
