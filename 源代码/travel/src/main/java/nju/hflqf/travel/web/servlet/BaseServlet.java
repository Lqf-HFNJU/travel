package nju.hflqf.travel.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseServlet extends HttpServlet {


    /**
     * 为了减少servlet数量，把操作同一张数据库表的servlet放在一起
     * 重写httpServlet方法的service方法，自定义方法分发
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求路径
        String uri = req.getRequestURI();   //返回/user/xxx
        //获取方法名称
        //需要忽视权限修饰符:getDeclaredMethod()
        //算了算了 还是改权限修饰符吧
        String methodName = uri.substring(uri.lastIndexOf('/') + 1);    //获取具体方法名
        //反射获取方法对象Method
        //this是userServlet的对象
        try {
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            //执行方法
            /*  别了别了
                //暴力反射
                //method.setAccessible(true);
            */
            method.invoke(this, req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 对象序列化为json,写回客户端
     * 子类可见
     */
    protected void writeValue(Object obj, HttpServletResponse response) throws IOException {//obj是要写成json并发送的东西
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getWriter(), obj);
    }

    /**
     * 对象序列化为json
     * @param obj 对象
     * @return json格式字符串
     */
    protected String writeValueAsString(Object obj, HttpServletResponse response) throws IOException {//obj是要写成json并发送的东西
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
       return mapper.writeValueAsString(obj);
    }
}
