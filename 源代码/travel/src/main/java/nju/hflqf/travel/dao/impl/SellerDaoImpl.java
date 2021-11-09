package nju.hflqf.travel.dao.impl;

import com.hflqf.jdbc.JdbcUtils;
import nju.hflqf.travel.dao.SellerDao;
import nju.hflqf.travel.domain.Seller;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;


public class SellerDaoImpl implements SellerDao {
    private JdbcTemplate jt = new JdbcTemplate(JdbcUtils.getDataSource());

    /**
     * 根据sid查询卖家对象
     */
    @Override
    public Seller findBySid(int sid) {
        String sql = "select * from tab_seller where sid=?";
        return jt.queryForObject(sql, new BeanPropertyRowMapper<>(Seller.class), sid);
    }
}
