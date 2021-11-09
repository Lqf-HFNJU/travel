package nju.hflqf.travel.dao.impl;

import com.hflqf.jdbc.JdbcUtils;
import nju.hflqf.travel.dao.FavouriteDao;
import nju.hflqf.travel.domain.Favorite;
import nju.hflqf.travel.domain.Route;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FavouriteDaoImpl implements FavouriteDao {
    JdbcTemplate jt = new JdbcTemplate(JdbcUtils.getDataSource());

    @Override
    public Favorite findByRidAndUid(int rid, int uid) {
        String sql = "select * from tab_favorite where rid=? and uid=?";
        Favorite favorite = null;
        try {
            favorite = jt.queryForObject(sql, new BeanPropertyRowMapper<>(Favorite.class), rid, uid);
        } catch (DataAccessException ignored) {
        }
        return favorite;
    }

    @Override
    public int findCountByRid(int rid) {
        String sql = "select count(*) from tab_favorite where rid=?";
        int num = 0;
        try {
            num = jt.queryForObject(sql, Integer.class, rid);
        } catch (Exception ignored) {
        }
        return num;
    }

    @Override
    public void add(int rid, int uid) {
        String sql = "insert into tab_favorite values(?,?,?)";
        jt.update(sql, rid, new Date(), uid);
    }

    @Override
    public List<Route> findAllFavorite(int uid, int start, int pageSize) {
        String sql = "select * from tab_favorite f left join tab_route r" +
                " on f.rid=r.rid where uid = ? limit ? , ?";
        return jt.query(sql, new BeanPropertyRowMapper<>(Route.class), uid, start, pageSize);
    }

    @Override
    public int findTotalCount(int uid) {
        String sql = "select count(*) from tab_favorite where uid=?";
        int num = 0;
        try {
            num = jt.queryForObject(sql, Integer.class, uid);
        } catch (Exception ignored) {
        }
        return num;
    }

    @Override
    public List<Route> findAllHot(String rname, int min, int max, int start, int pageSize) {
        String sql = "select count(f.rid),r.* from tab_favorite f " +
                "left join tab_route r on f.rid=r.rid where  1=1 and (r.price between ? and ? )";
        StringBuilder sb = new StringBuilder(sql);


        List params = new ArrayList();//条件们
        params.add(min);
        params.add(max);

        if (rname != null && rname.length() > 0 && !"null".equals(rname)) {
            sb.append(" and rname like ? ");
            params.add("%" + rname + "%");
        }
        sb.append("GROUP BY f.rid  ORDER BY count(f.rid) desc ");
        sb.append(" limit ? , ? ");//分页条件
        sql = sb.toString();

        params.add(start);
        params.add(pageSize);

        return jt.query(sql, new BeanPropertyRowMapper<>(Route.class), params.toArray());
    }

    @Override
    public int findHotCount(String rname, int min, int max) {
        String sql = "select count(*) from (select count(f.rid) ,r.* from tab_favorite f " +
                "left join tab_route r on f.rid=r.rid where  1=1 and (r.price between ? and ? )";
        StringBuilder sb = new StringBuilder(sql);


        List params = new ArrayList();//条件们
        params.add(min);
        params.add(max);

        if (rname != null && rname.length() > 0 && !"null".equals(rname)) {
            sb.append(" and rname like ? ");
            params.add("%" + rname + "%");
        }
        sb.append("GROUP BY f.rid ) a");
        sql = sb.toString();

        Integer num = jt.queryForObject(sql, Integer.class,params.toArray());
        return num == null ? 0 : num;

    }
}
