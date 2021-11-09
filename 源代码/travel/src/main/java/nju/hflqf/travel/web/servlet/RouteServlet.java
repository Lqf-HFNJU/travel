package nju.hflqf.travel.web.servlet;

import nju.hflqf.travel.domain.PageBean;
import nju.hflqf.travel.domain.Route;
import nju.hflqf.travel.domain.User;
import nju.hflqf.travel.service.FavoriteService;
import nju.hflqf.travel.service.RouteService;
import nju.hflqf.travel.service.impl.FavoriteServiceImpl;
import nju.hflqf.travel.service.impl.RouteServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    private final RouteService service = new RouteServiceImpl();
    private final FavoriteService favoriteService = new FavoriteServiceImpl();

    /**
     * 分页查询
     * cid 类别id
     * pageSize 每页几条
     * currentPage 当前第几页
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //获取数据并封装
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");

        //接收对应rname 线路名称
        String rname = request.getParameter("rname");
        //TODO: 2021/9/4 解决乱码！！！！！！
        if (rname != null) {
            rname = new String(rname.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        }


        int cid = 0;//默认值为0 也就是首页
        //日了，会传一个字符串的null
        if (cidStr != null && cidStr.length() > 0 && (!"null".equals(cidStr))) {
            cid = Integer.parseInt(cidStr);
        }
        int pageSize = 5;//默认为5条
        if (pageSizeStr != null && pageSizeStr.length() > 0 && (!"null".equals(pageSizeStr))) {
            pageSize = Integer.parseInt(pageSizeStr);
        }
        int currentPage = 1;//第一次访问是没有值的，默认为首页（第一页）
        if (currentPageStr != null && currentPageStr.length() > 0 && (!"null".equals(currentPageStr))) {
            currentPage = Integer.parseInt(currentPageStr);
        }

        /**
         * 注意一下：
         * 这里的rname并未做任何处理，可能为 null ,"","null"之类的不合法字符
         * 这里全部放行，留给dao处理
         */
        PageBean<Route> pb = service.pageQuery(cid, currentPage, pageSize, rname);

        writeValue(pb, response);
    }

    /**
     * 查询每一条线路的具体信息
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ridStr = request.getParameter("rid");
        int rid = 0;
        if (ridStr != null && ridStr.length() > 0 && !"null".equals(ridStr)) {
            rid = Integer.parseInt(ridStr);
        }
        Route route = service.findOne(rid);
        writeValue(route, response);
    }

    /**
     * 判断当前登录用户是否收藏过该线路
     * 其实应该在service里面做类型转换和非空判断
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ridStr = request.getParameter("rid");
        int rid = 0;
        if (ridStr != null && ridStr.length() > 0 && !"null".equals(ridStr)) {
            rid = Integer.parseInt(ridStr);
        }

        User user = (User) request.getSession().getAttribute("user");
        int uid = 0;//用户尚未登录就是0
        if (user != null) {//用户已经登录
            uid = user.getUid();
        }

        boolean flag = false;
        if (uid != 0) {//没登录就不查了，节省数据库开支
            flag = favoriteService.isFavourite(rid, uid);
        }

        writeValue(flag, response);

    }

    //添加收藏
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");

        User user = (User) request.getSession().getAttribute("user");
        int uid = 0;//用户尚未登录就是0
        if (user != null) {//用户已经登录
            uid = user.getUid();
        } else {
            return;
        }

        favoriteService.add(rid, uid);

    }

    /**
     * 分页查询单个用户的所有收藏
     * uid->rid->route
     * pageSize 每页几条
     * currentPage 当前第几页
     */
    public void findAllFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //获取数据并封装
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");

        int uid;
        try {
            User u = (User) request.getSession().getAttribute("user");
            uid = u.getUid();
        } catch (Exception e) {//没有uid，老子不找了
            return;
        }

        int pageSize = 8;//默认为8条
        if (pageSizeStr != null && pageSizeStr.length() > 0 && (!"null".equals(pageSizeStr))) {
            pageSize = Integer.parseInt(pageSizeStr);
        }
        int currentPage = 1;//第一次访问是没有值的，默认为首页（第一页）
        if (currentPageStr != null && currentPageStr.length() > 0 && (!"null".equals(currentPageStr))) {
            currentPage = Integer.parseInt(currentPageStr);
        }

        PageBean<Route> pb = favoriteService.findAllFavorite(uid, currentPage, pageSize);
        // System.out.println(pb.getList());
        writeValue(pb, response);


    }

    /**
     * 分页查询查出收藏排行榜
     */
    public void findAllHot(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取数据并封装
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String rname = request.getParameter("rname");
        String minStr = request.getParameter("min");
        String maxStr = request.getParameter("max");
        //TODO: 2021/9/4 解决乱码！！！！！！
        if (rname != null) {
            rname = new String(rname.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        }

        int min = 0;
        int max = 99999999;
        if (minStr != null && minStr.length() > 0 && !"null".equals(minStr)) {
            min = Integer.parseInt(minStr);
        }
        if (maxStr != null && maxStr.length() > 0 && !"null".equals(maxStr)) {
            max = Integer.parseInt(maxStr);
        }
        int pageSize = 8;//默认为8条
        if (pageSizeStr != null && pageSizeStr.length() > 0 && (!"null".equals(pageSizeStr))) {
            pageSize = Integer.parseInt(pageSizeStr);
        }
        int currentPage = 1;//第一次访问是没有值的，默认为首页（第一页）
        if (currentPageStr != null && currentPageStr.length() > 0 && (!"null".equals(currentPageStr))) {
            currentPage = Integer.parseInt(currentPageStr);
        }

        PageBean<Route> pb = favoriteService.findAllHot(rname, min, max, currentPage, pageSize);

        writeValue(pb, response);

    }

}
