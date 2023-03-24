package br.com.dbc.javamosdecolar.secutiry;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final TokenService tokenService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable()
                .and().cors()
                .and().csrf().disable()
                .authorizeHttpRequests((requisicao) ->
                        requisicao
                                // Companhias a definir
                                .antMatchers("/aviao**", "/passagem**").hasAuthority("ROLE_COMPANHIA")

                                // Companhia pode selecionar, atualizar e deletar a si mesmo
                                .antMatchers(HttpMethod.GET, "/companhia/**").hasAuthority("ROLE_COMPANHIA")
                                .antMatchers(HttpMethod.PUT, "/companhia/**").hasAuthority("ROLE_COMPANHIA")
                                .antMatchers(HttpMethod.DELETE, "/companhia/**").hasAuthority("ROLE_COMPANHIA")

                                // Companhia pode selecionar, atualizar e deletar
                                .antMatchers(HttpMethod.GET, "/voo/**").hasAuthority("ROLE_COMPANHIA")
                                .antMatchers(HttpMethod.PUT, "/voo/**").hasAuthority("ROLE_COMPANHIA")
                                .antMatchers(HttpMethod.DELETE, "/voo/**").hasAuthority("ROLE_COMPANHIA")

                                // Comprador pode selecionar, atualizar e deletar a si mesmo
                                .antMatchers("/comprador**").hasAuthority("ROLE_COMPRADOR")

                                // Companhia pode ver o histórico de vendas
                                .antMatchers(HttpMethod.GET,  "/venda/**/companhia").hasAuthority("ROLE_COMPANIHA")

                                // Ambos cancelam venda
                                .antMatchers(HttpMethod.DELETE,  "/venda").hasAnyAuthority("ROLE_COMPANIHA", "ROLE_COMPRADOR")

                                // Compradores podem vizualizar passagens disponíveis e suas compras
                                .antMatchers(HttpMethod.GET,  "/passagem/**", "/venda/**/comprador").hasAuthority("ROLE_COMPRADOR")

                                // Comprador pode realizar suas compras
                                .antMatchers(HttpMethod.POST,  "/venda").hasAuthority("ROLE_COMPRADOR")

                                // Admin é livre no sistema
                                .antMatchers("/**").hasAuthority("ROLE_ADMIN")

                                .anyRequest()
                                .authenticated());
        http.addFilterBefore(new TokenAuthenticationFilter(tokenService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers(
                "/",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/auth",
                "/auth/create")
                // Qualquer pessoa pode se cadastrar como companhia ou comprador
                .antMatchers(HttpMethod.POST, "/companhia", "/comprador");

    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedHeaders("*").allowedMethods("GET", "PUT", "DELETE", "POST");
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder () {
        return new StandardPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticaonManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}