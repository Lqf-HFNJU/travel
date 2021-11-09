package nju.hflqf.travel.dao.impl;

import com.hflqf.jdbc.JdbcUtils;
import nju.hflqf.travel.dao.RouteDao;
import nju.hflqf.travel.domain.Route;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate jt = new JdbcTemplate(JdbcUtils.getDataSource());

    /**
     * 查询所有数量
     * <p>
     * 定义一个sql模板来解决
     * 定义一个变长的参数集合
     *
     * @param cid   有可能为空！！！！！
     * @param rname 也有可能为空！！！！！
     */
    @Override
    public int findTotalCount(int cid, String rname) {
        //String sql = "select count(*) from tab_route where cid=?";

        //定义sql模板
        String sql = "select count(*) from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);

        List params = new ArrayList();//条件们
        //判断参数是否有值
        if (cid != 0) {
            sb.append(" and cid = ? ");
            params.add(cid);//添加？对应的值
        }

        /**
         * 由于 service层没有处理 rname的异常数据
         * 这里要进一步判断每一种错误的可能性
         */
        if (rname != null && rname.length() > 0 && !"null".equals(rname)) {
            sb.append(" and rname like ?");
            params.add("%" + rname + "%");
        }
        sql = sb.toString();


        return jt.queryForObject(sql, Integer.class, params.toArray());
    }

    /**
     * 同样如此
     *
     * @param cid   有可能为空！！！！！
     * @param rname 也有可能为空！！！！！
     */
    @Override
    public List<Route> findByPage(int cid, int start, int pageSize, String rname) {
        //定义sql模板
        String sql = "select * from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);

        List params = new ArrayList();//条件们
        //判断参数是否有值
        if (cid != 0) {
            sb.append(" and cid = ? ");
            params.add(cid);//添加？对应的值
        }
        if (rname != null && rname.length() > 0 && !"null".equals(rname)) {
            sb.append(" and rname like ? ");
            params.add("%" + rname + "%");
        }
        sb.append(" limit ? , ? ");//分页条件
        sql = sb.toString();

        params.add(start);
        params.add(pageSize);

        return jt.query(sql, new BeanPropertyRowMapper<>(Route.class), params.toArray());
    }

    @Override
    public Route findOne(int rid) {
        String sql = "select * from tab_route where rid =?";
        return jt.queryForObject(sql, new BeanPropertyRowMapper<>(Route.class), rid);
    }

}
