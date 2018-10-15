/*
 * class: EncodingFilter
 */

package by.epam.admission.controller.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import java.io.IOException;

/**
 * Class EncodingFilter is used to set HTTP request and response character
 * encoding to 'UTF-8'
 *
 * @author Burishinets Maxim
 * @version 1.0 29 Aug 2018
 */
@WebFilter(filterName = "encodingFilter", urlPatterns = {"/*"}, initParams = {
        @WebInitParam(
                name = "encoding",
                value = "UTF-8",
                description = "Encoding param"
        )
})
public class EncodingFilter implements Filter {

    private static final Logger LOG = LogManager.getLogger(EncodingFilter.class);

    private String code;

    /**
     * Initializes Filter object depending on filter initialization parameters
     *
     * @param filterConfig - filter configuration object
     * @throws ServletException Defines a general exception a servlet can throw
     * when it encounters difficulty
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        code = filterConfig.getInitParameter("encoding");
    }

    /**
     * Method sets character encoding type for each servlet request/response
     * object to 'UTF-8'
     *
     * @param servletRequest - Defines an object to provide client request
     *                       information to a servlet. The servlet container
     *                       creates a ServletRequest object and passes it as
     *                       an argument to the servlet's service method
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
     *                   of the chain
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
        String codeRequest = servletRequest.getCharacterEncoding();
        if (code != null && !code.equalsIgnoreCase(codeRequest)) {
            servletRequest.setCharacterEncoding(code);
            servletResponse.setCharacterEncoding(code);
        }
        LOG.debug(getClass().getSimpleName() + " worked");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        code = null;
    }
}
