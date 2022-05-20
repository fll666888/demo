package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * 访问 http://localhost:18080/demo/swagger-ui.html
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    //swagger2的配置文件，这里可以配置swagger2的一些基本的内容，比如扫描的包等等
    @Bean
    public Docket createRestApi() {
        // 添加请求参数，我们这里把token作为请求头部参数传入后端
        ParameterBuilder parameterBuilder = new ParameterBuilder();
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameterBuilder.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build();
        parameters.add(parameterBuilder.build());
        return new Docket(DocumentationType.SWAGGER_2)
                //设置包含在json ResourceListing响应中的api元信息
                .apiInfo(apiInfo())
                //启动用于api选择的构建器
                .select()
                //扫描api接口的包 这里用;隔开可以写多个RequestHandlerSelectors.basePackage("路径;路径;路径")
                .apis(RequestHandlerSelectors.basePackage("com.example.demo.controller"))
                //路径过滤器（扫描所有路径）
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(parameters);
    }

    //构建api文档的详细信息函数
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("Spring Boot 测试使用 Swagger2 构建RESTful API")
                //描述
                .description("API 描述")
                //版本号
                .version("1.0.0")
                //创建人信息
                .contact(new Contact("MrFan",  "",  "413950045@qq.com"))
                .build();
    }

}
