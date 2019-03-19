package gov.idaho.isp.forensic.time.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.csrf.CsrfException;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

/**
 * Intended to fix the CSRF Timeout Caveat (https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#csrf-timeouts).
 * Unlike the <code>MissingCsrfTokenAccessDeniedHandler</code> this implementation
 * will redirect the user to login on any CsrfException encountered, not just
 * MissingCsfTokenException.  The intended result is that no loss of data due to
 * session timeout or an invalid CSRF token being sent to the server (presumably
 * due to token expiration).
 */
public class CsrfExceptionAccessDeniedHandler extends AccessDeniedHandlerImpl {
  private RequestCache requestCache = new HttpSessionRequestCache();
  private String loginPage = "/login";

  @Override
  public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException exception) throws IOException, ServletException {
    if (exception instanceof CsrfException) {
      requestCache.saveRequest(req, res);
      res.sendRedirect(req.getContextPath() + loginPage);
    }
    super.handle(req, res, exception);
  }

  public void setRequestCache(RequestCache requestCache) {
    this.requestCache = requestCache;
  }

  public void setLoginPage(String loginPage) {
    this.loginPage = loginPage;
  }
}
