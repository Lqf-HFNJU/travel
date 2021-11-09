package nju.hflqf.travel.service;

import nju.hflqf.travel.domain.PageBean;
import nju.hflqf.travel.domain.Route;

/**
 * 线路service
 */
public interface RouteService {
    /**
     * 根据类别进行分页查询
     */
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname);

    /**
     * 根据rid查询所有信息
     * 查三张表
     */
    Route findOne(int rid);
}
