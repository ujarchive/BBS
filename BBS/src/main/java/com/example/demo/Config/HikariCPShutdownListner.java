package com.easynetworks.lotteFactoring.Config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HikariCPShutdownListner implements ApplicationListener<ContextClosedEvent> {

    @Autowired
    private HikariDataSource dataSource;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        if (dataSource != null) {
            dataSource.close();
            log.debug("HikariCP datasource closed.");
        }
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }
}
