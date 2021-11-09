package nju.hflqf.travel.web.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
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

@WebServlet("/user/*")  //通配符 访问/user的所有资源
public class UserServlet extends BaseServlet {

    //声明 UserService 业务对象
    private UserService service = new UserServiceImpl();

    /**
     * 注册功能
     */
    public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //先判断验证码是否正确
        if (checkCode(request, response)) {
            Map<String, String[]> map = request.getParameterMap();
            User user = new User();
            try {
                BeanUtils.populate(user, map);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            //UserService service = new UserServiceImpl();
            boolean flag = service.register(user);
            ResultInfo info = new ResultInfo();
            if (flag) {//注册成功
                info.setFlag(true);
                System.out.println(user.getName()+"完成注册");
            } else {//注册失败
                info.setFlag(false);
                info.setErrorMsg("注册失败!");
            }

            //info对象序列化为json,写回客户端
            writeValue(info, response);
        }
    }

    /**
     * 登录功能
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //先判断验证码是否正确,为真才继续执行
        if (checkCode(request, response)) {
            Map<String, String[]> map = request.getParameterMap();
            User user = new User();
            try {
                BeanUtils.populate(user, map);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            //UserService service = new UserServiceImpl();
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
                //设置登录状态的session
                request.getSession().setAttribute("user", u);
                System.out.println(u.getName()+"登录");
            }

            writeValue(info, response);

        }
    }

    /**
     * 查找一个
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object user = request.getSession().getAttribute("user");
        writeValue(user, response);
    }

    /**
     * 退出
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println(((User)request.getSession().getAttribute("user")).getName()+"退出登录");
        request.getSession().invalidate();//session自杀方法
        response.sendRedirect(request.getContextPath() + "/login.html");
    }

    /**
     * 激活
     */
    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        if (code != null) {
            //UserService service = new UserServiceImpl();
            boolean flag = service.active(code);

            String msg = null;
            if (flag) {
                msg = "激活成功,请<a href='../login.html'>登录</a>";
            } else {
                msg = "激活失败,请联系管理员！";
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }
    }

    /**
     * 判断验证码是否正确
     *
     * @return true表示正确，false表示错误
     */
    private boolean checkCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String check = request.getParameter("check");
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)) {//不成功
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码错误！请刷新验证码再试");
            response.setContentType("application/json;charset=utf-8");
            //info对象序列化为json,写回客户端
            writeValue(info, response);
            return false;
        } else {
            return true;
        }
    }

}
