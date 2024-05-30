package com.example.roombookingonline.config;

import com.example.roombookingonline.handler.CustomAuthenticationFailureHandler;
import com.example.roombookingonline.handler.CustomAuthenticationSuccessHandler;
import com.example.roombookingonline.handler.CustomLogoutSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Configuration
//    @Order(2)
//    public static class App1ConfigurationAdapter {
//
//        @Bean
//        public SecurityFilterChain filterChainApp1(HttpSecurity http) throws Exception {
//            http.csrf(AbstractHttpConfigurer::disable)
//                    .authorizeHttpRequests(auth ->
//                            auth.requestMatchers("/manager*")
//                                    .hasRole("MANAGER")
//                                    .requestMatchers("/manager/login").permitAll()
//                                    .anyRequest()
//                                    .authenticated()
//                    )
//                    .formLogin(formLogin ->
//                            formLogin.loginPage("/manager/login")
//                                    .loginProcessingUrl("/manager_login")
//                                    .defaultSuccessUrl("/manager")
//                                    .failureUrl("/manager/login?error=loginError")
//                    )
//                    .logout(logout ->
//                            logout.logoutUrl("/manager_logout")
//                                    .logoutSuccessUrl("/manager/login")
//                                    .deleteCookies("JSESSIONID")
//                    );
//
//            return http.build();
//        }
//    }
//
//    @Configuration
//    @Order(3)
//    public static class App2ConfigurationAdapter {
//
//        @Bean
//        public SecurityFilterChain filterChainApp2(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
//            http.csrf(AbstractHttpConfigurer::disable)
//                    .authorizeHttpRequests(auth ->
//                            auth.requestMatchers("/receptionist*")
//                                    .hasRole("RECEPTIONIST")
//                                    .anyRequest()
//                                    .authenticated()
//                    )
//                    .formLogin(formLogin ->
//                            formLogin.loginPage("/receptionist/login")
//                                    .loginProcessingUrl("/receptionist_login")
//                                    .defaultSuccessUrl("/receptionist")
//                                    .failureUrl("/manager/login?error=loginError")
//                    )
//                    .logout(logout ->
//                            logout.logoutUrl("/receptionist_logout")
//                                    .logoutSuccessUrl("/receptionist/login")
//                                    .deleteCookies("JSESSIONID")
//                    );
//            return http.build();
//        }
//    }
//
//    @Configuration
//    @Order(4)
//    public static class App3ConfigurationAdapter {
//
//        @Bean
//        public SecurityFilterChain filterChainApp3 (HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
//            http.csrf(AbstractHttpConfigurer::disable)
//                    .authorizeHttpRequests(auth ->
//                            auth.requestMatchers("/user*")
//                                    .hasRole("CUSTOMER")
//                                    .anyRequest()
//                                    .authenticated()
//                    )
//                    .formLogin(formLogin ->
//                            formLogin.loginPage("/user/login")
//                                    .loginProcessingUrl("/user_login")
//                                    .defaultSuccessUrl("/home")
//                                    .failureUrl("/user/login?error=loginError")
//                    )
//                    .logout(logout ->
//                            logout.logoutUrl("/user_logout")
//                                    .logoutSuccessUrl("/home")
//                                    .deleteCookies("JSESSIONID")
//                    );
//            return http.build();
//        }
//    }
//    @Configuration
//    @Order(1)
//    public static class App4ConfigurationAdapter {
//        @Bean
//        public SecurityFilterChain filterChainApp4(HttpSecurity http) throws Exception {
//            http.csrf(AbstractHttpConfigurer::disable)
//                    .authorizeHttpRequests(auth ->
//                            auth.requestMatchers("manager/login", "receptionist/login", "user/login",
//                                            "/home", "/search", "rooms**", "/register", "/resources**", "/booking-cart**")
//                                    .permitAll()
//                    );
//            return http.build();
//        }
//    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(a ->{
                   a.requestMatchers("/","/home", "/rooms/**","/search", "/booking-cart/**","/manager/login", "/receptionist/login",
                                   "/register", "/user/login", "/resources/**")
                           .permitAll()
                           .requestMatchers("/manager/**").hasRole("MANAGER")
                           .requestMatchers("/receptionist/**").hasRole("RECEPTIONIST")
                           .requestMatchers("/user/**", "/payment/**").hasRole("CUSTOMER")
                           .anyRequest().authenticated();
                }).formLogin(formLogin ->{
                        formLogin.loginPage("/user/login")
                                .loginProcessingUrl("/process_login")
                                .successHandler(authenticationSuccessHandler())
                                .failureHandler(authenticationFailureHandler());
                }).logout(logout ->{
                    logout.logoutUrl("/process_logout")
                            .deleteCookies("JSESSIONID")
                            .logoutSuccessUrl("/user/login")
//                            .logoutSuccessHandler(logoutSuccessHandler())
                    ;
                })
        ;
        return http.build();
    }

    @Bean
    public CustomAuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public CustomAuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public CustomLogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }


    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("manager1"));
    }
}
