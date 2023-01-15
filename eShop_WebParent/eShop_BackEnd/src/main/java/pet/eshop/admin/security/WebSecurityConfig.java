package pet.eshop.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new EShopUserDetailsService();
    }

    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.authorizeRequests().anyRequest().permitAll();    //  позволяет логиниться минуя окно авторизации
        http.authorizeRequests()
                .antMatchers("/users/**", "/settings/**", "/countries/**", "/states/**")
                                .hasAuthority("Admin")         // права авторизации
                .antMatchers("/categories/**" , "/brands/**")
                                .hasAnyAuthority("Admin", "Editor")
                .antMatchers("/products/new", "products/delete/**")
                                .hasAnyAuthority("Admin", "Editor")
                .antMatchers("/products/edit/**", "/products/save", "/products/check_unique")
                                .hasAnyAuthority("Admin", "Editor", "Salesperson")
                .antMatchers("/products", "/products/", "/products/detail/**","/products/page/**")
                                .hasAnyAuthority("Admin", "Editor","Salesperson", "Shipper")
                .antMatchers("/products/**")
                                .hasAnyAuthority("Admin", "Editor")
                .antMatchers("/shipping/**", "/customers/**", "/orders/**", "/get_shipping_cost")
                                .hasAnyAuthority("Admin", "Salesperson")
                .anyRequest().authenticated()
                .and()
                    .formLogin()
                        .loginPage("/login")
                        .usernameParameter("email")                     // вместо логина для авторизации
                        .permitAll()
                .and().logout().permitAll()                         // для логаута
                .and()
                    .rememberMe()                                 // включаем возможность "remember me"
                        .key("AbcDefgHijklmNOprs_1234567890")           // теперь cookies сохраняются при рестарте приложения
                        .tokenValiditySeconds(7 * 24 * 60 * 60)     // время жизни cookies
                .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);

        http.headers().frameOptions().sameOrigin();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // игнорим папки с картинками, js и бутстрап в странице секьюрити
        web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
    }
}
