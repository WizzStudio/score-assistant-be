package com.wz.score_assistant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@ComponentScan(basePackages = {"com.wz.score_assistant.controller"})
@EnableSwagger2
@EnableWebMvc
public class SwaggerConfiguration {
    @Bean
    public Docket api(Environment environment) {
        Profiles profiles = Profiles.of("dev","test");
        boolean flag = environment.acceptsProfiles(profiles);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(flag)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wz.score_assistant.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    @Bean
    public RequestMappingInfoHandlerMapping requestMapping(){
        return new RequestMappingHandlerMapping();
    }
    private ApiInfo apiInfo() {
        Contact contact = new Contact("祁圳鑫","","1500418656@qq.com");
        return new ApiInfoBuilder()
                .title("学分助手 -在线API接口文档")
                .description("来自为之工作室的学分助手后端API接口文档")
                .contact(contact)
                .build();
    }
}
