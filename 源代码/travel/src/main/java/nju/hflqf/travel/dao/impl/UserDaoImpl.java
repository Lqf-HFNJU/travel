package nju.hflqf.travel.dao.impl;

import com.hflqf.jdbc.JdbcUtils;
import nju.hflqf.travel.dao.UserDao;
import nju.hflqf.travel.domain.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 根据用户名查询用户信息
 */
public class UserDaoImpl implements UserDao {
    private JdbcTemplate jt = new JdbcTemplate(JdbcUtils.getDataSource());

    @Override
    public User findByUsername(String name) {
        User user = null;
        try {
            String sql = "select username from tab_user where username=?";
            user = jt.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), name);
        } catch (Exception e) {
        }
        return user;
    }

    public void save(User user) {
        String sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code) values(?,?,?,?,?,?,?,?,?)";
        jt.update(sql,
                user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getBirthday(),
                user.getSex(),
                user.getTelephone(),
                user.getEmail(),
                user.getStatus(),
                user.getCode());
    }

    /**
     * 根据激活码查询用户对象
     */
    @Override
    public User findByCode(String code) {
        String sql = "select * from tab_user where code=?";
        User user = null;
        try {
            user = jt.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), code);
        } catch (DataAccessException ignored) {
        }
        return user;
    }

    /**
     * 修改指定用户激活对象
     */
    @Override
    public void updateStatus(User user) {
        String sql = "update tab_user set status='Y' where uid=?";
        jt.update(sql, user.getUid());
    }

    /**
     * 通过用户名的密码查询用户
     */
    @Override
    public User findByUsernameAndPassword(String username, String password) {
        String sql = "select * from tab_user where username=? and password=?";
        User user = null;
        try {
            user = jt.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), username, password);
        } catch (DataAccessException ignored) {
        }
        return user;
    }
}
