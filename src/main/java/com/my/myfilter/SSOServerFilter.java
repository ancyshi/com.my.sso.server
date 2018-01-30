package com.my.myfilter;

import com.my.cache.CookieCache;
import com.my.model.CookieId;
import com.my.util.ToolsUtil;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/25.
 */
@Order(1)
//重点
@WebFilter(filterName = "ssoServerFilter", urlPatterns ={ "/server/page/login"})
public class SSOServerFilter  extends HttpServlet implements Filter {
    @Resource
    private CookieCache cookieCache;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        // 验证全局会话是否存在且有效

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String globalSessionId = ToolsUtil.getCookieValueByName(request, "globalSessionId");
        if (globalSessionId != null && !this.verifyGlobalSession(globalSessionId)) {
            request.setAttribute("globalSessionIdCheck","false");
        } else if (globalSessionId == null  ) {
            request.setAttribute("globalSessionIdCheck","false");
        }
            chain.doFilter(request,  response);
    }

    private boolean verifyGlobalSession(String globalSessionId) {
        CookieId cookieId =    cookieCache.getCookie(globalSessionId);
        if (cookieId != null ) {
            return true;
        }
        return false;
    }

}
