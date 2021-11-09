package nju.hflqf.travel.service;

import nju.hflqf.travel.domain.PageBean;
import nju.hflqf.travel.domain.Route;


public interface FavoriteService {
    /**
     * 找是否收藏
     */
    public boolean isFavourite(int rid, int uid);

    /**
     * 根据 rid和 uid添加收藏
     *
     * @param rid 字符串！！！
     * @param uid
     */
    void add(String rid, int uid);

    /**
     * 根据 uid 查询用户全部的收藏
     *
     * @param uid
     * @param currentPage
     * @param pageSize
     * @return
     */
    PageBean<Route> findAllFavorite(int uid, int currentPage, int pageSize);

    /**
     * 查询收藏热度排行榜
     * 可能需要根据字段和价格来筛选
     * 根据 rid的排序查询出所有记录
     * 两张表联合查询
     */
    PageBean<Route> findAllHot(String rname, int min, int max, int currentPage, int pageSize);
}
