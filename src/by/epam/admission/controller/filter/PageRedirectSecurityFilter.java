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
 * Class PageRedirectSecurityFilter prevents unauthorized access to the
 * application through the browser line
 *
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

    /**
     * Methods retrieves HTTP request header 'Referer' and checks it for 'null'
     * value. If it's true - depending on session's role occurs redirection to
     * home page or error page (with status code 403 - forbidden)
     *
     * @param servletRequest - Defines an object to provide client request
     *                      information to a servlet. The servlet container
     *                      creates a ServletRequest object and passes it as
     *                      an argument to the servlet's service method
     * @param servletResponse - Defines an object to assist a servlet in sending
     *                       a response to the client. The servlet container
     *                       creates a ServletResponse object and passes it as
     *                       an argument to the servlet's service method
     * @param filterChain - A FilterChain is an object provided by the servlet
     *                   container to the developer giving a view into the
     *                   invocation chain of a filtered request for a resource.
     *                   Filters use the FilterChain to invoke the next filter
     *                   in the chain, or if the calling filter is the last
     *                   filter in the chain, to invoke the resource at the end
     *                   of the chain.
     * @throws IOException Signals that an I/O exception of some sort has
     * occurred
     * @throws ServletException Defines a general exception a servlet can throw
     * when it encounters difficulty
     */
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
