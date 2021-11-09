package nju.hflqf.travel.web.servlet;

import nju.hflqf.travel.domain.Category;
import nju.hflqf.travel.domain.Route;
import nju.hflqf.travel.service.CategoryService;
import nju.hflqf.travel.service.impl.CategoryServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/category/*")
public class CategoryServlet extends BaseServlet {
    private CategoryService service = new CategoryServiceImpl();

    /**
     * 查询所有
     */
    public void findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> cs = service.findAll();
        writeValue(cs, response);
    }


    /**
     * 根据 rid找category
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");

        Category category = service.findOne(rid);
        writeValue(category, response);
    }
}
