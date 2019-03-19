package gov.idaho.isp.forensic.time.controller.interceptor;

import java.util.function.Function;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * If SecurityContext has an authenticated Principal of type UserDetails, that
 * UserDetails object is placed into the request using the configurable
 * userRequestAttributeName key (default is key value is "user")
 *
 * Users can override this default behavior by supplying their own getUserFunction
 * which takes an Authentication and returns and object to store as a request
 * attribute by the userRequestAttributeName key
 */
public class SpringSecurityUserInterceptor extends HandlerInterceptorAdapter {
  private String userRequestAttributeName = "user";
  private Function<Authentication,Object> getUserFunction = auth -> auth != null && auth.getPrincipal() instanceof UserDetails ? auth.getPrincipal() : null;

  public SpringSecurityUserInterceptor() {
  }

  public SpringSecurityUserInterceptor(String userRequestAttributeName) {
    this.userRequestAttributeName = userRequestAttributeName;
  }

  public SpringSecurityUserInterceptor(Function<Authentication,Object> getUserFunction) {
    this.getUserFunction = getUserFunction;
  }

  public SpringSecurityUserInterceptor(String userRequestAttributeName, Function<Authentication,Object> getUserFunction) {
    this.userRequestAttributeName = userRequestAttributeName;
    this.getUserFunction = getUserFunction;
  }

  @Override
  public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
    if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
      final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      req.setAttribute(userRequestAttributeName, getUserFunction.apply(authentication));
    }
    return true;
  }

  public String getUserRequestAttributeName() {
    return userRequestAttributeName;
  }

  public void setUserRequestAttributeName(String userRequestAttributeName) {
    this.userRequestAttributeName = userRequestAttributeName;
  }
}