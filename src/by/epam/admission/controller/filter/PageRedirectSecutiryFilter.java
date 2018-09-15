package by.epam.admission.controller.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "PageRedirectFilter", urlPatterns = "/jsp/*",
        initParams = {
        @WebInitParam(name = "INDEX_PATH", value = "/index.jsp")
})
public class PageRedirectSecutiryFilter implements Filter {

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

        LOG.debug("raw link redirect filter");

        httpResponse.sendRedirect(httpRequest.getContextPath() + indexPath);

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
