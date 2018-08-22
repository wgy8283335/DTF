package com.coconason.dtf.client.core.nettyclient.nettyserverconfig;

/**
 * @Author: Jason
 * @date: 2018/8/22-17:22
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:nettyserver.properties")
public class NettyServerConfiguration {
    @Value(value="${nettyserver.host}")
    private String host;
    @Value(value="${nettyserver.port}")
    private Integer port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
