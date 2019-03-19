package gov.idaho.isp.forensic.time.controller.interceptor;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Sets as request attributes the map's entries (string key, object values).
 */
public class RequestAttributeInterceptor extends HandlerInterceptorAdapter {
  private final Map<String,?> requestAttributes;

  public RequestAttributeInterceptor(Map<String,?> requestAttributes) {
    this.requestAttributes = requestAttributes;
  }

  @Override
  public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
    requestAttributes.entrySet().forEach(e -> {
      req.setAttribute(e.getKey(), e.getValue());
    });
    return true;
  }
}
