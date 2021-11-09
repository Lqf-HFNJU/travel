package nju.hflqf.travel.dao.impl;

import com.hflqf.jdbc.JdbcUtils;
import nju.hflqf.travel.dao.RouteImgDao;
import nju.hflqf.travel.domain.RouteImg;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class RouteImgDaoImpl implements RouteImgDao {
    private JdbcTemplate jt = new JdbcTemplate(JdbcUtils.getDataSource());

    @Override
    public List<RouteImg> findByRid(int rid) {
        String sql = "select * from tab_route_img where rid=?";
        return jt.query(sql, new BeanPropertyRowMapper<>(RouteImg.class), rid);
    }
}
