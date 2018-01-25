package com.my.myfilter;

import com.my.util.ToolsUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Administrator on 2018/1/25.
 */
public class SSOServerFilter  extends HttpServlet implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 验证全局会话是否存在且有效

        // 有效的话就doChain，在方法中生成了全局会话和token

        // 无效就重定向到登录界面
    }
}
