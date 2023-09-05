package com.vagas.cadastro.config;

import com.vagas.cadastro.model.Usuario;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class SpringFoxConfig {

    @Bean
    public Docket swagger() {

        Set<String> protocols
                = new HashSet<>();
        protocols.add("http");
        protocols.add("https");

        RequestParameterBuilder aParameterBuilder = new RequestParameterBuilder();
        aParameterBuilder.name("Authorization")
                .query(q -> q.defaultValue("Token")
                        .model(modelSpecificationBuilder -> modelSpecificationBuilder.scalarModel(ScalarType.STRING)))
                .in(ParameterType.HEADER).required(true).build();

        List<RequestParameter> aParameters = new ArrayList<>();
        aParameters.add(aParameterBuilder.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .protocols(protocols)
                .groupName("IFRS")
                .ignoredParameterTypes(Usuario.class)
                .globalRequestParameters(aParameters);
    }
}
