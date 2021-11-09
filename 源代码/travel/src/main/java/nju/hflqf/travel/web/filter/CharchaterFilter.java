package nju.hflqf.travel.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.HandshakeRequest;
import java.io.IOException;

/**
 * 字符集编码过滤器
 */
@WebFilter("/*")
public class CharchaterFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        //父接口换成子接口
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        //获取请求方法
        String method = req.getMethod();
        if (method.equalsIgnoreCase("post") || method.equalsIgnoreCase("get")) {
            req.setCharacterEncoding("utf-8");
        }
        //处理响应乱码
        res.setContentType("text/html;charset=utf-8");
        chain.doFilter(req, res);
    }
}
