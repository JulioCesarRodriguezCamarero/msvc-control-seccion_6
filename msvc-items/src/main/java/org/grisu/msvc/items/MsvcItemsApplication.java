package org.grisu.msvc.items;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EntityScan({"org.grisu.libs.msvc.commons.entities"})
public class MsvcItemsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsvcItemsApplication.class, args);
    }

}
