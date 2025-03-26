package example.login.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class MyFirstFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        System.out.println("첫번째 필터: 요청이 들어왔습니다." );
        if ("true".equals(httpServletRequest.getParameter("block"))) {
            httpServletResponse.sendError(403, "막혔어요!");
            return;
        }
        System.out.println(httpServletRequest.getRequestURI());
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("첫번째 필터: 응답이 나갑니다.");
//        httpServletResponse.getWriter().write("[필터에 적은 내용]"); 나중에
    }
}
