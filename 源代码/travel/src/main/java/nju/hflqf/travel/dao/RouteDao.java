package nju.hflqf.travel.dao;

import nju.hflqf.travel.domain.Route;

import java.util.List;

public interface RouteDao {
    /**
     * rname:路线名称
     * cid:菜单栏id
     * 根据cid rname查询总记录数
     */
    public int findTotalCount(int cid, String rname);

    /**
     * 根据cid start pageSize rname
     * 查询当前页的数据集合
     */
    public List<Route> findByPage(int cid, int start, int pageSize, String rname);

    /**
     * 根据rid查询具体的线路信息
     */
    public Route findOne(int rid);


}
