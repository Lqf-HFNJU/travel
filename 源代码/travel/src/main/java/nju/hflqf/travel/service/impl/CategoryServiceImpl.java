package nju.hflqf.travel.service.impl;

import com.hflqf.jedis.JedisPoolUtils;
import nju.hflqf.travel.dao.CategoryDao;
import nju.hflqf.travel.dao.RouteDao;
import nju.hflqf.travel.dao.impl.CategoryDaoImpl;
import nju.hflqf.travel.dao.impl.RouteDaoImpl;
import nju.hflqf.travel.domain.Category;
import nju.hflqf.travel.domain.Route;
import nju.hflqf.travel.service.CategoryService;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {
    private final CategoryDao dao = new CategoryDaoImpl();
    private final RouteDao routeDao = new RouteDaoImpl();


    /**
     * 先从 redis查
     * 如果查询的集合为空，再从 MySQL里面查
     */
    @Override
    public List<Category> findAll() {
        Jedis jedis = JedisPoolUtils.getJedis();
        //使用sortedset来排序查询
        Set<Tuple> css = jedis.zrangeWithScores("category", 0, -1);
        List<Category> cls = null;
        if (css == null || css.size() == 0) {//查询的集合为空

            System.out.println("从数据库查询");

            cls = dao.findAll();
            for (Category c : cls) {//写入redis
                jedis.zadd("category", c.getCid(), c.getCname());
            }
        } else {//将set存入list

            System.out.println("从redis查询");

            cls = new ArrayList<>();
            for (Tuple t : css) {
                Category c = new Category();
                c.setCname(t.getElement());
                c.setCid((int) t.getScore());
                cls.add(c);
            }
        }
        return cls;
    }

    @Override
    public Category findOne(String ridStr) {
        int rid = 0;
        Route route = null;
        if (ridStr != null && ridStr.length() > 0 && !"null".equals(ridStr)) {
            rid = Integer.parseInt(ridStr);
        } else {
            return null;
        }
        route = routeDao.findOne(rid);
        return dao.findByCid(route.getCid());
    }
}
