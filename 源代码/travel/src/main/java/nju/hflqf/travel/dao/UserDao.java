package nju.hflqf.travel.dao;

import nju.hflqf.travel.domain.User;

public interface UserDao {
    /**
     * 根据用户名查询用户信息
     */
    public User findByUsername(String name);

    /**
     * 用户保存
     * @param user
     */
    public void save(User user);

    /**
     * 根据激活码查询用户对象
     * @param code 激活码
     * @return 返回用户对象
     */
    User findByCode(String code);

    /**
     * 修改指定用户激活对象
     * @param user 指定用户对象
     */
    void updateStatus(User user);

    /**
     * 通过用户名的密码查询用户
     * @param username 用户名
     * @param password 密码
     */
    User findByUsernameAndPassword(String username,String password);

}
