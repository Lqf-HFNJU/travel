package nju.hflqf.travel.dao;

import nju.hflqf.travel.domain.Favorite;
import nju.hflqf.travel.domain.Route;

import java.util.List;

public interface FavouriteDao {

    /**
     * 根据 rid和 uid
     * 找一个 favorite对象
     */
    public Favorite findByRidAndUid(int rid, int uid);

    /**
     * 根据 rid找出被收藏的个数
     *
     * @param rid
     * @return
     */
    public int findCountByRid(int rid);


    /**
     * 根据 rid和 uid添加收藏
     */
    void add(int rid, int uid);

    /**
     * 根据 uid 查询用户所有收藏
     * 两张表联合查询
     */
    List<Route> findAllFavorite(int uid, int start, int pageSize);

    /**
     * 根据 uid 查询有多少条收藏记录
     *
     * @param uid
     * @return
     */
    int findTotalCount(int uid);

    /**
     * 查询收藏热度排行榜
     * 可能需要根据字段和价格来筛选
     * 根据 rid的排序查询出所有记录
     * 两张表联合查询
     */
    List<Route> findAllHot(String rname, int min, int max, int start, int pageSize);

    /**
     * 查询有多少条收藏热度数
     */
    int findHotCount(String rname, int min, int max);
}
