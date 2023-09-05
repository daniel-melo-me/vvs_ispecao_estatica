package com.vagas.cadastro.config;

import com.vagas.cadastro.repository.UsuarioRepository;
import com.vagas.cadastro.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final TokenService tokenService;
    private final UsuarioRepository repository;

    //Configurações de Autenticação
    @Bean
    protected AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    private static final String[] PUBLIC_URL = {
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v2/api-docs",
            "/swagger-resources/configuration/ui",
            "/swagger-resources",
            "/swagger-resources/configuration/security",
            "/swagger-ui.html",
            "/swagger-ui",
    };

    //Configurações de Autorização
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable().cors().disable().authorizeHttpRequests()
                .antMatchers(HttpMethod.POST, "/auth/*").permitAll()
                .antMatchers(HttpMethod.GET, "/auth/*").permitAll()
                .antMatchers(HttpMethod.POST, "/arquivo/uploadFile").permitAll()
                .antMatchers(HttpMethod.GET, "/arquivo/downloadFile/*").permitAll()
                .antMatchers(HttpMethod.POST, "/email/enviar").permitAll()
                .antMatchers(PUBLIC_URL).permitAll()
                .anyRequest().authenticated().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, repository), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    protected BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
