/*
 * Copyright (c) 2025 Karlsruhe Institute of Technology.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.kit.datamanager.hector25.tora_game_management_service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan
@EnableJpaRepositories
@EnableTransactionManagement
@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "Tora Game Management Service API",
                version = "0.0.1-SNAPSHOT",
                description = "API for the Tora Game Management Service.",
                license = @io.swagger.v3.oas.annotations.info.License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                )
        )
)
public class ToraGameManagementServiceApplication {

    static void main(String[] args) {
        SpringApplication.run(ToraGameManagementServiceApplication.class, args);
        String address = "%s://%s:%s".formatted(System.getProperty("server.protocol", "http"), System.getProperty("server.address", "localhost"), java.lang.System.getProperty("server.port", "8080"));
        System.out.printf("\nTora Game Management Service started. Running on %s%n", address);
        System.out.printf("Swagger UI available at %s/swagger-ui.html%n%n \n", address);
    }
}
