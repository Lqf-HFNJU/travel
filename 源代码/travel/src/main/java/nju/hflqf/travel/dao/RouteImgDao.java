package nju.hflqf.travel.dao;

import nju.hflqf.travel.domain.RouteImg;

import java.util.List;

/**
 * 查询图片的dao
 */
public interface RouteImgDao {
    /**
     * 根据route的id查询图片集合
     */
    public List<RouteImg> findByRid(int rid);
}
