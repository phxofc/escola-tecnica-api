package com.escolatecnica.api.configuration;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class FlywayConfiguration {

    @Bean
    @Profile("dev")
    public FlywayMigrationStrategy cleanMigrateStrategy() {
        return Flyway::migrate;
    }

    @Bean
    @Profile("staging")
    public FlywayMigrationStrategy stagingCleanMigrateStrategy() {
        return Flyway::migrate;
    }
}

