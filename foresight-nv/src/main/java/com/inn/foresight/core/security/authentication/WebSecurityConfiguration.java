package com.inn.foresight.core.security.authentication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.encoder.AESUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.foresight.core.generic.utils.ConfigEnum;

/**
 * The Class WebSecurityConfiguration.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  /** The Constant logger. */
  private static final Logger logger = LogManager.getLogger(WebSecurityConfiguration.class);

  /** The Constant DEFAULT_USER_ROLE. */
  private static final String DEFAULT_USER_ROLE = "USER";

  /** The Constant DEFAULT_BASIC_AUTHENTICATION_USERNAME. */
  private static final String DEFAULT_BASIC_AUTHENTICATION_USERNAME = "Hg9uJe2oWCxlVOpOgP4TGw==";

  /** The Constant DEFAULT_BASIC_AUTHENTICATION_PASSWORD. */
  private static final String DEFAULT_BASIC_AUTHENTICATION_PASSWORD =
      "YZzG2iTgcThJP+njbeOOSNqIMbJV9c6spskp7tFRoeE=";


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    String restUrl = "/*/rest/secure/**";
    logger.info("Inside configure method , restUrl pattern {}", restUrl);
    http.csrf().disable().antMatcher(restUrl).authorizeRequests().anyRequest().authenticated().and()
        .httpBasic();
  }

  /**
   * Configure global.
   *
   * @param auth the auth
   * @throws Exception the exception
   */
  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

    String userName = ConfigUtils.getString(ConfigEnum.BASIC_AUTHENTICATION_USER_NAME.getValue());
    String password = ConfigUtils.getString(ConfigEnum.BASIC_AUTHENTICATION_PASSWORD.getValue());

    logger.info("Configurable Username {} and password {}", userName, password);

    if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
      logger.info(
          "Configurable username or password is empty therfore setting default username and password");
      userName = DEFAULT_BASIC_AUTHENTICATION_USERNAME;
      password = DEFAULT_BASIC_AUTHENTICATION_PASSWORD;
    }

    userName = AESUtils.decrypt(userName);
    password = AESUtils.decrypt(password);

    auth.inMemoryAuthentication().withUser(userName).password(password).roles(DEFAULT_USER_ROLE);
  }

}
