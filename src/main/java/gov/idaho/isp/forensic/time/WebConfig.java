package gov.idaho.isp.forensic.time;

import gov.idaho.isp.forensic.time.controller.interceptor.RequestAttributeInterceptor;
import gov.idaho.isp.forensic.time.controller.interceptor.SpringSecurityUserInterceptor;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Value("${spring.profiles.active}") private String activeProfile;

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/login").setViewName("login");
    registry.addViewController("/bs4-showcase").setViewName("bs4-showcase");
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new SpringSecurityUserInterceptor("user"));
    registry.addInterceptor(new RequestAttributeInterceptor(getRequestAttributes()));
  }

  private Map<String,Object> getRequestAttributes() {
    Map<String,Object> requestAttributes = new HashMap<>();
    requestAttributes.put("activeProfile", activeProfile);
    requestAttributes.put("shortDateFmt", DateTimeFormatter.ofPattern("MM/dd"));
    return requestAttributes;
  }
}