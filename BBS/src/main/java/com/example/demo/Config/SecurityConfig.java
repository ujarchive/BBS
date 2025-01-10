package com.example.demo.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true) // @PreAuthorize 사용 가능
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final CustomLoginAuthenticationEntrypoint authenticationEntryPoint;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomAccessDeniedHander accessDeniedHandler;
    private final CustomLogoutHandler customLogoutHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        /**
         * 1. CustomAuthenticationFilter 에서 설정한 URL 로 사용자가 요청을 하면 해당 필터가 요청을 가로챕니다.
         * 2. 여기서 인증되지 않은 CustomAuthenticationToken 을 생성하고, AuthenticationManager 에게 인증처리를
         *     요청합니다. 여기서 Token 에 사용자가 건낸 아이디와 패스워드를 보관합니다.
         * 3. AuthenticationManger 는 우리가 AuthenticationProvider 에게 인증처리를 건냅니다.
         *   여기서 우리가 만든 CustomAuthenticationProvider 가 동작합니다.
         * 4. CustomAuthenticationProvider 에서 CustomUserDetailsService 로 DB 에 있는 User 정보를 가져와
         *     AuthenticationToken 에 저장한 사용자 정보가 일치하는지 확인하여 일치하면 SuccessHandler 를 실패하면
         *     FailedHandler 를 실행합니다. 여기까지 과정중에 에러가 발생하면 FailedHandler 로 가게됩니다.
         * 5. 인증 성공 후 기본 로직에 의해 인가 확인을 합니다.
         *    ( url을 등록하고 hasRole("USER") 와 같이 설정하였을때, 인가 확인을 합니다. )
         * 6. 인가 실패시 경우에 따라 해당 Handler 를 실행합니다.
         *     CustomLoginAuthenticationEntrypoint
         *     CustomAccessDeniedHander
         *    인증 성공시 거치지 않음.
         */
        

        http
                .addFilterBefore(ajaxAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new RequestCachingFilter(), CustomAuthenticationFilter.class) //RequestCachingFilter를 추가하여 요청 본문을 캐시할 수 있도록 설정
                .exceptionHandling(config -> config
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler));


        /**
         * 사용자 권한별 경로 냐누기 */
        http
                .authorizeRequests()
                /**
                 * Swagger 관련 접근 경로 허용
                 */
                .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html").permitAll()

                .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/manager/**")).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/manager/**")).hasRole("MANAGER")
                .requestMatchers("/**").permitAll()
                .requestMatchers("/css/**","/js/**","/images/**","/vendor/**").permitAll()

                .requestMatchers("/api/**").authenticated()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                /** 로그인한 사용자만 마이페이지에 접근할 수 있도록 마이페이지에 대한 권한을 설정 **/
                .requestMatchers("/api/myInfo/**").authenticated()
                .requestMatchers("/myInfo/**").authenticated()
                .anyRequest().authenticated();
        /**
         * 로그아웃 핸들러 */
        http
                .logout(logout -> logout
                        .logoutUrl("/api/logout")  // 로그아웃 요청 URL
                        .addLogoutHandler(customLogoutHandler)// 커스텀 로그아웃 핸들러 추가
                        .invalidateHttpSession(true)  // 세션 무효화
                        .deleteCookies("JSESSIONID","Remember-me")  // JSESSIONID 쿠키 삭제
                        .logoutSuccessHandler((request, response, authentication) -> {
                            new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK);
                            response.sendRedirect("/");
                        })
                );
//        http.logout(httpSecurityLogoutConfigurer ->
//                httpSecurityLogoutConfigurer.logoutUrl("/api/logout").logoutSuccessUrl("/login").deleteCookies("JSESSIONID","Remember-me"));

        /**
         * 세션 관리 */
        http.sessionManagement(httpSecuritySessionManagementConfigurer ->
                httpSecuritySessionManagementConfigurer.maximumSessions(1)
                        .maxSessionsPreventsLogin(false).expiredUrl("/login")); //false - 새 로그인이 성공하며, 기존 세션은 강제로 종료, true - 새 로그인 시도가 거부되고, 기존 세션은 유지

       // http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));
//        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
        http.cors(Customizer.withDefaults());
        http.csrf(CsrfConfigurer::disable);


        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CustomAuthenticationFilter ajaxAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter();
        customAuthenticationFilter.setAuthenticationManager(authenticationManager());
        customAuthenticationFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        customAuthenticationFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);

        // **
        customAuthenticationFilter.setSecurityContextRepository(
                new DelegatingSecurityContextRepository(
                        new RequestAttributeSecurityContextRepository(),
                        new HttpSessionSecurityContextRepository()
                ));

        return customAuthenticationFilter;
    }
}
