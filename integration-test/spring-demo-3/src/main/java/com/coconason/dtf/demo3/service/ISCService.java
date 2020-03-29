package com.coconason.dtf.demo3.service;

/**
 * @Author: Jason
 * @date: 2018/8/27-15:00
 */

import com.coconason.dtf.demo3.model.DemoResult;

public interface ISCService {
    DemoResult addSCInfoWithoutDtf(Sc course) throws Exception;
    
    DemoResult addSCInfo(Sc course) throws Exception;

    DemoResult addSCInfoStrong(Sc course) throws Exception;

    DemoResult addSCInfoAsync(Sc course) throws Exception;

    Sc getSCInfo(int id) throws Exception;

}
