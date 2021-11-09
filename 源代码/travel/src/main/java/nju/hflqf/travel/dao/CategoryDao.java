package nju.hflqf.travel.dao;

import nju.hflqf.travel.domain.Category;
import nju.hflqf.travel.domain.Route;

import java.util.List;


public interface CategoryDao {
    /**
     * 查询所有category信息
     */
    public List<Category> findAll();

    /**
     * 根据cid查询具体的菜单信息
     */
    public Category findByCid(int cid);

}
