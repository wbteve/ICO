package com.tongwii.ico.configurer;

import com.tongwii.ico.security.JWTAuthenticationFilter;
import com.tongwii.ico.security.JwtAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author : Zeral
 * @date : 2017/8/3
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity( prePostEnabled = true )
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.profiles.active}")
    private String env;//当前激活的配置文件

    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureAuthentication ( AuthenticationManagerBuilder authenticationManagerBuilder ) throws Exception {
        authenticationManagerBuilder
                .userDetailsService( this.userDetailsService )
                .passwordEncoder( passwordEncoder() );
    }

    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTAuthenticationFilter authenticationTokenFilterBean () throws AuthenticationException {
        return new JWTAuthenticationFilter();
    }

    @Override
    protected void configure ( HttpSecurity httpSecurity ) throws Exception {
        // 开发者模式，放行所有
        if (env.equals("dev")) {
            httpSecurity
                    // jwt不需要csrf
                    .csrf().disable()
                    // cors 支持
                    .cors().and()
                    // jwt不需要session , 所以不创建会话
                    .sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS ).and()
                    // 允许授权请求
                    .authorizeRequests()
                    // 允许匿名资源请求
                    .anyRequest().permitAll();
        } else {
            httpSecurity
                    // jwt不需要csrf
                    .csrf().disable()
                    // cors 支持
                    .cors().and()
                    // jwt不需要session , 所以不创建会话
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    // 异常处理
                    .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and()
                    // 允许授权请求
                    .authorizeRequests()
                    // 允许匿名资源请求
                    .antMatchers(
                            HttpMethod.GET,
                            "/",
                            "/*.html",
                            "/favicon.ico",
                            "/assets/**",
                            "/**/*.html",
                            "/**/*.css",
                            "/**/*.js"
                    ).permitAll()
                    // 认证放行
                    .antMatchers(HttpMethod.POST, "/auth/**").permitAll()
                    .antMatchers(HttpMethod.PUT, "/auth/**").permitAll()
                    // 除上面外的所有请求全部需要鉴权认证
                    .anyRequest().authenticated();
            // 基于定制JWT安全过滤器
            httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
        }
        // 禁用页面缓存
        httpSecurity.headers().cacheControl();
    }
}