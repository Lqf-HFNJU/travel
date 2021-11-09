package nju.hflqf.travel.service;

import nju.hflqf.travel.domain.User;

public interface UserService {
    /**
     * 注册用户
     *
     * @return true表示注册成功
     */
    boolean register(User user);

    /**
     * 激活用户
     *
     * @param code 激活码
     * @return 返回值表示能否正常激活
     */
    boolean active(String code);

    /**
     * 用户登录
     * @param user 只有用户名和密码的参数
     * @return 一个比较全的user
     */
    User login(User user);

}
