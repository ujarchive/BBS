package com.example.demo.Config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataSourceShutdownHook implements DisposableBean {
    @Override
    public void destroy() throws Exception {
        log.debug("Closing HikariCP datasource");
    }
}
