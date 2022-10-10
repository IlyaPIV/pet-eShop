package pet.eshop.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll();    //  позволяет логиниться минуя окно авторизации
//        http.authorizeRequests()
//                .antMatchers("/users/**").hasAuthority("Admin")         // права авторизации
//                .antMatchers("/categories/**" , "/brands/**").hasAnyAuthority("Admin", "Editor")
//                .antMatchers("/products", "/products/", "/products/detail/**","/products/page/**")
//                                .hasAnyAuthority("Admin", "Editor","Salesperson", "Shipper")
//                .antMatchers("/products/new", "products/delete/**")
//                                .hasAnyAuthority("Admin", "Editor")
//                .antMatchers("/products/edit/**", "/products/save", "/products/check_unique")
//                                .hasAnyAuthority("Admin", "Editor", "Salesperson")
//                .antMatchers("/products/**").hasAnyAuthority("Admin", "Editor")
//                .anyRequest().authenticated()
//                .and()
//                    .formLogin().loginPage("/login")
//                    .usernameParameter("email")                     // вместо логина для авторизации
//                    .permitAll()
//                .and().logout().permitAll()                         // для логаута
//                .and().rememberMe()                                 // включаем возможность "remember me"
//                    .key("AbcDefgHijklmNOprs_1234567890")           // теперь cookies сохраняются при рестарте приложения
//                        .tokenValiditySeconds(7 * 24 * 60 * 60);    // время жизни cookies
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // игнорим папки с картинками, js и бутстрап в странице секьюрити
        web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
    }
}
