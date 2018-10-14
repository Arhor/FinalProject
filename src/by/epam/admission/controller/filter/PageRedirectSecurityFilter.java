/*
 * class: PageRedirectSecurityFilter
 */

package by.epam.admission.controller.filter;

import by.epam.admission.model.User;
import by.epam.admission.util.ConfigurationManager;
import by.epam.admission.util.Names;
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
           urlPatterns = {"/jsp/*", "/admission/*"})
public class PageRedirectSecurityFilter implements Filter {

    private static final Logger LOG = LogManager.getLogger();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        HttpSession session = httpRequest.getSession();
        User.Role role = (User.Role) session.getAttribute(Names.ROLE);

        String referer = httpRequest.getHeader(Names.REFERER);

        LOG.debug(referer);

        if (referer == null) {
            if (role == User.Role.ADMIN || role == User.Role.CLIENT) {
                httpResponse.sendRedirect("/controller?command=home");
            } else {
                httpResponse.sendError(403);
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
