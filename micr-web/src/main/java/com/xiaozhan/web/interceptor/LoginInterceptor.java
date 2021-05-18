package com.xiaozhan.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.xiaozhan.common.CommonUtils;
import com.xiaozhan.contants.Code;
import com.xiaozhan.contants.CommonContants;
import com.xiaozhan.licai.model.User;
import com.xiaozhan.vo.ResultObject;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute(CommonContants.SESSION_USER_KEY);
        if (user == null) {
            //获取请求头 X-Requested-With: XMLHttpRequest
            String header = request.getHeader("X-Requested-With");
            System.out.println("header===="+header);
            if ("XMLHttpRequest".equals(header)) {
                //ajax请求，返回数据
                ResultObject ro = new ResultObject();
                ro.setCode(Code.NOT_LOGIN);
                ro.setMessage("请求登录");
                response.setContentType("application/json;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.println(JSONObject.toJSONString(ro));
                out.flush();
                out.close();
            } else {
                //没有登录， 跳转到登录页面      /loan/page/login
                request.getRequestDispatcher("/loan/page/login").forward(request, response);
            }
            return false;

        }
        return true;
    }
}
