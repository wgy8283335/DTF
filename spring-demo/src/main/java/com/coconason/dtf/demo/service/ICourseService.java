package com.coconason.dtf.demo.service;

/**
 * @Author: Jason
 * @date: 2018/8/27-15:00
 */

import com.coconason.dtf.demo.model.DemoResult;
import com.coconason.dtf.demo.po.Course;

public interface ICourseService {
    DemoResult addCourseInfo(Course course) throws Exception;

    DemoResult addCourseInfoAsync(Course course) throws Exception;

}
