package nju.hflqf.travel.service.impl;

import nju.hflqf.travel.dao.FavouriteDao;
import nju.hflqf.travel.dao.impl.FavouriteDaoImpl;
import nju.hflqf.travel.domain.Favorite;
import nju.hflqf.travel.domain.PageBean;
import nju.hflqf.travel.domain.Route;
import nju.hflqf.travel.service.FavoriteService;

import java.util.List;

public class FavoriteServiceImpl implements FavoriteService {
    private FavouriteDao favouriteDao = new FavouriteDaoImpl();

    /**
     * 找是否收藏
     */
    @Override
    public boolean isFavourite(int rid, int uid) {
        Favorite favorite = favouriteDao.findByRidAndUid(rid, uid);
        return favorite != null;//如果对象有值，则为true
    }

    @Override
    public void add(String rid, int uid) {
        if (rid != null && rid.length() > 0 && !"null".equals(rid)) {
            favouriteDao.add(Integer.parseInt(rid), uid);
        }

    }

    @Override
    public PageBean<Route> findAllFavorite(int uid, int currentPage, int pageSize) {
        //封装pageBean
        PageBean<Route> pb = new PageBean<>();
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);

        //总记录数
        int totalCount = favouriteDao.findTotalCount(uid);
        pb.setTotalCount(totalCount);

        //当前页面数据集合
        //uid,开始的记录数，每页条数
        List<Route> list = favouriteDao.findAllFavorite(uid, (currentPage - 1) * pageSize, pageSize);
        pb.setList(list);

        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
        pb.setTotalPage(totalPage);

        return pb;

    }

    @Override
    public PageBean<Route> findAllHot(String rname, int min, int max, int currentPage, int pageSize) {
        //封装pageBean
        PageBean<Route> pb = new PageBean<>();
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);

        //当前页面数据集合
        List<Route> list = favouriteDao.findAllHot(rname, min, max, (currentPage - 1) * pageSize, pageSize);
        //另外分别查询收藏次数
        for (Route r : list) {
            int cnt = favouriteDao.findCountByRid(r.getRid());
            r.setCnt(cnt);
        }
        pb.setList(list);

        //总记录数
        int totalCount = favouriteDao.findHotCount(rname, min, max);
        pb.setTotalCount(totalCount);


        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
        pb.setTotalPage(totalPage);

        return pb;
    }
}
