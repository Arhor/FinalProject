package by.epam.admission.filter;

import by.epam.admission.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "loginFilter")
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpsResponse = (HttpServletResponse) servletResponse;
        HttpSession session = httpRequest.getSession();
        User.Role type = (User.Role) session.getAttribute("userType");
        if (type == null) {
            type = User.Role.GUEST;
            session.setAttribute("userType", type);
            RequestDispatcher dispatcher = servletRequest.getServletContext().getRequestDispatcher("/jsp/guest.jsp");
            dispatcher.forward(httpRequest, httpsResponse);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
