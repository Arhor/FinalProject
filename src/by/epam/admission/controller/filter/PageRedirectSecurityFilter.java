/*
 * class: PageRedirectSecurityFilter
 */

package by.epam.admission.controller.filter;

import by.epam.admission.model.User;
import by.epam.admission.util.ConfigurationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.TreeSet;

/**
 * @author Burishinets Maxim
 * @version 1.0 29 Aug 2018
 */
@WebFilter(filterName = "pageRedirectFilter",
        urlPatterns = {"/jsp/*", "/admission/*"},
        initParams = {
        @WebInitParam(name = "INDEX_PATH", value = "/index.jsp")
})
public class PageRedirectSecurityFilter implements Filter {

    private static final Logger LOG = LogManager.getLogger();

    private String indexPath;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        indexPath = filterConfig.getInitParameter("INDEX_PATH");
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        HttpSession session = httpRequest.getSession();
        User.Role role = (User.Role) session.getAttribute("role");

        String referer = httpRequest.getHeader("Referer");

        LOG.debug(referer);

        if (referer == null) {
            if (role == User.Role.ADMIN || role == User.Role.CLIENT) {
                httpResponse.sendRedirect("/controller?command=home");
            } else {
                httpResponse.sendRedirect(httpRequest.getContextPath() + indexPath);
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}