package nju.hflqf.travel.dao;


import nju.hflqf.travel.domain.Seller;


/**
 * 查询卖家的dao
 */
public interface SellerDao {
    /**
     * 根据sid查询卖家对象
     */
    public Seller findBySid(int sid);
}
