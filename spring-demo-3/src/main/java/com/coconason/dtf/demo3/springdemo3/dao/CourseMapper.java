package com.coconason.dtf.demo3.springdemo3.dao;


import com.coconason.dtf.demo3.springdemo3.po.Course;
import com.coconason.dtf.demo3.springdemo3.po.CourseExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table Course
     *
     * @mbggenerated Mon Aug 27 14:24:30 CST 2018
     */
    int countByExample(CourseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table Course
     *
     * @mbggenerated Mon Aug 27 14:24:30 CST 2018
     */
    int deleteByExample(CourseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table Course
     *
     * @mbggenerated Mon Aug 27 14:24:30 CST 2018
     */
    int insert(Course record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table Course
     *
     * @mbggenerated Mon Aug 27 14:24:30 CST 2018
     */
    int insertSelective(Course record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table Course
     *
     * @mbggenerated Mon Aug 27 14:24:30 CST 2018
     */
    List<Course> selectByExample(CourseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table Course
     *
     * @mbggenerated Mon Aug 27 14:24:30 CST 2018
     */
    int updateByExampleSelective(@Param("record") Course record, @Param("example") CourseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table Course
     *
     * @mbggenerated Mon Aug 27 14:24:30 CST 2018
     */
    int updateByExample(@Param("record") Course record, @Param("example") CourseExample example);
}