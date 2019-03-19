package gov.idaho.isp.forensic.time;

import gov.idaho.isp.forensic.time.domain.User;
import gov.idaho.isp.forensic.time.domain.User.Role;
import gov.idaho.isp.forensic.time.security.CsrfExceptionAccessDeniedHandler;
import gov.idaho.isp.forensic.time.security.CustomInMemoryUserDetailsManager;
import gov.idaho.isp.forensic.time.service.DatabaseUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  DatabaseUserDetailsService databaseUserDetailsService;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // when not requiring h2 console access remove this line:
    allowAdminAccessToH2Console(http);

    http.authorizeRequests()
      .antMatchers("/webjars/**").permitAll()
      .antMatchers("/assets/**").permitAll()
      .antMatchers("/admin/**").hasAuthority("ADMIN")
      .anyRequest().authenticated()
      .and().formLogin().loginPage("/login").permitAll()
      .and().logout().logoutUrl("/logout").permitAll()
      .and().exceptionHandling().accessDeniedHandler(getAccessDeniedHandler());
  }

  private void allowAdminAccessToH2Console(HttpSecurity http) throws Exception {
    http.csrf().ignoringAntMatchers("/h2-console/**");
    http.headers().frameOptions().disable();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // in production do not use in memory authentication:
    configureInMemoryAuthentication(auth);
    configureDbAuthentication(auth);
  }

  private void configureInMemoryAuthentication(AuthenticationManagerBuilder auth) throws Exception {
    User user = new User();
    user.setFirstName("Joe");
    user.setLastName("Administrator");
    user.setEmail("joe.admin@email.com");
    user.setUsername("admin");
    user.setPassword(getPasswordEncoder().encode("admin"));
    user.setUserRole(Role.ADMIN);
    auth.userDetailsService(new CustomInMemoryUserDetailsManager(user));
  }

  private void configureDbAuthentication(AuthenticationManagerBuilder auth) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(databaseUserDetailsService);
    provider.setPasswordEncoder(getPasswordEncoder());
    auth.authenticationProvider(provider);
  }

  @Bean
  public PasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  private AccessDeniedHandler getAccessDeniedHandler() {
    return new CsrfExceptionAccessDeniedHandler();
  }
}
