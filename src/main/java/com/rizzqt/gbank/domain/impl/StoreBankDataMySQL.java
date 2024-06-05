package com.rizzqt.gbank.domain.impl;

import com.zaxxer.hikari.HikariConfig;

public class StoreBankDataMySQL extends StoreBankDataDB {

    @Override
    public HikariConfig getHikariConfig() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getDatabaseName());
        hikariConfig.setUsername(getUserName());
        hikariConfig.setPassword(getPassword());
        return hikariConfig;
    }

}