//package com.interview.demo.config;
//
//import com.interview.demo.error.ApiError;
//import com.interview.demo.error.ApiErrorCode;
//import io.micrometer.core.instrument.util.IOUtils;
//import io.swagger.v3.core.converter.ModelConverters;
//import io.swagger.v3.oas.models.Components;
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.security.SecurityRequirement;
//import io.swagger.v3.oas.models.security.SecurityScheme;
//import io.swagger.v3.oas.models.servers.Server;
//import lombok.extern.slf4j.Slf4j;
//import org.springdoc.core.GroupedOpenApi;
//import org.springdoc.core.customizers.OpenApiCustomiser;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ResourceLoader;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//import java.util.Arrays;
//
//@Configuration
//@Slf4j
//public class OpenApiConfiguration {
////    static {
////        SpringDocUtils.getConfig().replaceWithSchema(LocalTime.class, new Schema<LocalTime>()
////                .example(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
////                .type("string"));
////    }
//
//    @Value("${openapi.server.url}")
//    private String serverUrl;
//
//    @Value("${openapi.server.description}")
//    private String serverDesc;
//
//    @Autowired
//    private ResourceLoader resourceLoader;
//
//
//    @Bean
//    public OpenAPI openAPI() {
//        return new OpenAPI()
//                .info(getDefaultApiInfo())
//                .servers(Arrays.asList(new Server().url(serverUrl).description(serverDesc)))
//                .components(new Components()
//                        .addSecuritySchemes("BearerToken", new SecurityScheme()
//                                .type(SecurityScheme.Type.HTTP)
//                                .scheme("bearer").bearerFormat("JWT")))
//                .security(Arrays.asList(
//                        new SecurityRequirement().addList("BearerToken"))
//                );
//
//    }
//
//    private Info getDefaultApiInfo() {
//        try (InputStream descIn = resourceLoader.getResource("classpath:default-open-api-desc.md").getInputStream()) {
//            String desc = IOUtils.toString(descIn, StandardCharsets.UTF_8);
//            return new Info().title("QBurger Open API").description(desc);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//    private OpenApiCustomiser openAPICustomiser(Info info) {
//        ModelConverters modelConverter = ModelConverters.getInstance();
//        return openApi -> {
//            openApi.setInfo(info);
//            modelConverter.readAllAsResolvedSchema(ApiError.class)
//                    .referencedSchemas.entrySet().forEach(s -> openApi.getComponents().addSchemas(s.getKey(), s.getValue()));
//            modelConverter.readAllAsResolvedSchema(ApiErrorCode.class)
//                    .referencedSchemas.entrySet().forEach(s -> openApi.getComponents().addSchemas(s.getKey(), s.getValue()));
//        };
//    }
//
//    @Bean
//    public GroupedOpenApi defaultGroup() {
//        return GroupedOpenApi.builder()
//                .group("default")
//                .packagesToScan("com.interview.demo.controller", "com.interview.demo.model", "com.interview.demo.error")
//                .pathsToMatch("/api/**")
//                .addOpenApiCustomiser(openAPICustomiser(getDefaultApiInfo()))
//                .build();
//    }
//
//    @Bean
//    public GroupedOpenApi adminGroup() {
//        return GroupedOpenApi.builder()
//                .group("admin")
//                .packagesToScan("com.interview.demo.controller", "com.interview.demo.model", "com.interview.demo.error")
//                .pathsToMatch("/api/admin/**")
//                .addOpenApiCustomiser(openAPICustomiser(getDefaultApiInfo()))
//                .build();
//    }
//
//    @Bean
//    public GroupedOpenApi customerGroup() {
//        return GroupedOpenApi.builder()
//                .group("customer")
//                .packagesToScan("com.interview.demo.controller", "com.interview.demo.model", "com.interview.demo.error")
//                .pathsToMatch("/api/customer/**")
//                .addOpenApiCustomiser(openAPICustomiser(getDefaultApiInfo()))
//                .build();
//    }
//
//}
