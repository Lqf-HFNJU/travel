package nju.hflqf.travel.dao.impl;

import com.hflqf.jdbc.JdbcUtils;
import nju.hflqf.travel.dao.CategoryDao;
import nju.hflqf.travel.domain.Category;
import nju.hflqf.travel.domain.Route;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class CategoryDaoImpl implements CategoryDao {
    JdbcTemplate jt = new JdbcTemplate(JdbcUtils.getDataSource());

    @Override
    public List<Category> findAll() {
        String sql = "select * from tab_category";
        return jt.query(sql, new BeanPropertyRowMapper<>(Category.class));
    }

    @Override
    public Category findByCid(int cid) {
        String sql = "select * from tab_category where cid=?";
        return jt.queryForObject(sql, new BeanPropertyRowMapper<>(Category.class), cid);
    }

}
