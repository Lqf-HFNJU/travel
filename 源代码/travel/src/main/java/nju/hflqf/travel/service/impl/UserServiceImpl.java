package nju.hflqf.travel.service.impl;

import com.hflqf.mail.MailUtils;
import com.hflqf.uuid.UuidUtil;
import nju.hflqf.travel.dao.UserDao;
import nju.hflqf.travel.dao.impl.UserDaoImpl;
import nju.hflqf.travel.domain.User;
import nju.hflqf.travel.service.UserService;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();

    /**
     * 注册用户
     *
     * @return true表示注册成功
     */
    @Override
    public boolean register(User user) {
        User u = userDao.findByUsername(user.getUsername());
        if (u != null) {//用户名存在
            return false;
        } else {//保存用户
            //设置激活码，唯一
            //使用uuid工具类
            user.setCode(UuidUtil.getUuid());
            //设置激活状态
            user.setStatus("N");
            userDao.save(user);

            //发送邮件，激活
            String content = null;
            try {
                content = "<a href='http://" + Inet4Address.getLocalHost().getHostAddress() + "//travel/user/active?code="
                        + user.getCode() + "'>点击激活【华附旅游网】</a>";
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            MailUtils.sendMail(user.getEmail(), content, "激活邮件");
            return true;
        }
    }

    /**
     * 激活用户
     */
    @Override
    public boolean active(String code) {
        User user = userDao.findByCode(code);
        if (user != null) {//用户存在，可以激活
            userDao.updateStatus(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User login(User user) {
        return userDao.findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }

}





