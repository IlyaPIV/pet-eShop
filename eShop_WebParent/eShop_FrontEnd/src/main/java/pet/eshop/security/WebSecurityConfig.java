package pet.eshop.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pet.eshop.security.oauth.CustomerOAuth2UserService;
import pet.eshop.security.oauth.OAuth2LoginSuccessHandler;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomerOAuth2UserService oAuth2UserService;
    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginHandler;
    @Autowired
    private DatabaseLoginSuccessHandler databaseLoginHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new CustomerUserDetailsService();
    }

    public DaoAuthenticationProvider authenticationProvider(){
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
//        http.authorizeRequests().anyRequest().permitAll();    //  позволяет логиниться минуя окно авторизации
        http.authorizeRequests()
                .antMatchers("/account_details", "/update_account_details",
                        "/cart", "/address_book/**").authenticated()   // только для авторизованных
                .anyRequest().permitAll()                               // всё остальное и без авторизации
                .and()
                .formLogin()
                    .loginPage("/login")
                    .usernameParameter("email")                     // вместо логина для авторизации
                    .successHandler(databaseLoginHandler)
                    .permitAll()
                .and()
                .oauth2Login()
                    .loginPage("/login")
                    .userInfoEndpoint()
                    .userService(oAuth2UserService)
                    .and()
                    .successHandler(oAuth2LoginHandler)
                .and()
                .logout().permitAll()                         // для логаута
                .and()
                .rememberMe()                                 // включаем возможность "remember me"
                    .key("1234567890_AbcDefgHijklmNOprs")           // теперь cookies сохраняются при рестарте приложения
                        .tokenValiditySeconds(7 * 24 * 60 * 60);    // время жизни cookies
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // игнорим папки с картинками, js и бутстрап в странице секьюрити
        web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
    }
}
