package nju.hflqf.travel.service.impl;

import nju.hflqf.travel.dao.*;
import nju.hflqf.travel.dao.impl.*;
import nju.hflqf.travel.domain.*;
import nju.hflqf.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao routeDao = new RouteDaoImpl();
    private RouteImgDao routeImgDao = new RouteImgDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();
    private CategoryDao categoryDao = new CategoryDaoImpl();
    private FavouriteDao favouriteDao = new FavouriteDaoImpl();

    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname) {
        //封装pageBean
        PageBean<Route> pb = new PageBean<>();
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);

        //总记录数
        int totalCount = routeDao.findTotalCount(cid, rname);
        pb.setTotalCount(totalCount);

        //当前页面数据集合
        List<Route> list = routeDao.findByPage(cid, (currentPage - 1) * pageSize, pageSize, rname);//cid,开始的记录数，每页条数
        pb.setList(list);

        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
        pb.setTotalPage(totalPage);

        return pb;
    }

    /**
     * 根据rid 所有信息，也就是查四张表
     *
     * @param rid
     * @return
     */
    @Override
    public Route findOne(int rid) {
        //根据rid查tab_route表
        Route route = routeDao.findOne(rid);

        //根据rid查tab_route_img表，图片的集合信息
        List<RouteImg> imgList = routeImgDao.findByRid(rid);
        route.setRouteImgList(imgList);

        //根据rid查询卖家信息 (sid)
        Seller seller = sellerDao.findBySid(route.getSid());
        route.setSeller(seller);

        //根据cid查询菜单栏信息 (cid)
        Category category = categoryDao.findByCid(route.getCid());
        route.setCategory(category);

        //查询收藏的数量
        int cnt = favouriteDao.findCountByRid(rid);
        route.setCnt(cnt);

        return route;
    }
}
