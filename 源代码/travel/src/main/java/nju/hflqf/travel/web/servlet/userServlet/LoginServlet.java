package nju.hflqf.travel.web.servlet.userServlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import nju.hflqf.travel.domain.ResultInfo;
import nju.hflqf.travel.domain.User;
import nju.hflqf.travel.service.UserService;
import nju.hflqf.travel.service.impl.UserServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //先判断验证码是否正确
        String check = request.getParameter("check");
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)) {//不成功
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码错误！");
            response.setContentType("application/json;charset=utf-8");
            //info对象序列化为json,写回客户端
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
            response.getWriter().write(json);
            return;
        }
        Map<String, String[]> map = request.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        UserService service = new UserServiceImpl();
        User u = service.login(user);

        ResultInfo info = new ResultInfo();

        //判断各种情况
        //有没有这个用户？用户有没有激活？
        if (u == null) {//用户名密码错误
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误！");
        } else if (!"Y".equals(u.getStatus())) {//没激活
            info.setFlag(false);
            info.setErrorMsg("您尚未激活，请激活！");
        } else {
            info.setFlag(true);
            session.setAttribute("user", u);
        }

        response.setContentType("application/json;charset=utf-8");
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), info);


    }
}
