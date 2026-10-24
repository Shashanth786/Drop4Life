
package com.drop4life.config;

import java.io.IOException;
import java.util.Set;

import com.drop4life.model.User;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AuthenticationFilter implements Filter {

    private static final Set<String> PROTECTED_PATHS = Set.of(
            "/HTML/a.jsp",
            "/HTML/profile.html",
            "/HTML/donateblood.html",
            "/HTML/requestblood.html",
            "/HTML/donors.html",
            "/HTML/recipients.html",
            "/HTML/admin-dashboard.html",
            "/requestSuccess",
            "/donorSuccess"
    );

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest =
                (HttpServletRequest) request;

        HttpServletResponse httpResponse =
                (HttpServletResponse) response;

        String contextPath = httpRequest.getContextPath();

        String path = httpRequest.getRequestURI()
                .substring(contextPath.length());

        if (PROTECTED_PATHS.contains(path)) {

            // Prevent browser caching of protected pages
            httpResponse.setHeader(
                    "Cache-Control",
                    "no-store, no-cache, must-revalidate, max-age=0");

            httpResponse.setHeader("Pragma", "no-cache");
            httpResponse.setDateHeader("Expires", 0);

            HttpSession session = httpRequest.getSession(false);

            // Require login
            if (session == null
                    || session.getAttribute("loggedInUser") == null) {

                httpResponse.sendRedirect(
                        contextPath + "/login.html");
                return;
            }

            // Require ADMIN role for the Admin Dashboard
            if ("/HTML/admin-dashboard.html".equals(path)) {

                Object loggedIn =
                        session.getAttribute("loggedInUser");

                if (!(loggedIn instanceof User)
                        || !"ADMIN".equalsIgnoreCase(
                                ((User) loggedIn).getRole())) {

                    httpResponse.sendError(
                            HttpServletResponse.SC_FORBIDDEN,
                            "Administrator access required");
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }
}
