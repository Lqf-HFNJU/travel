package nju.hflqf.travel.service;

import nju.hflqf.travel.domain.Category;
import nju.hflqf.travel.domain.Route;

import java.util.List;

public interface CategoryService {

    public List<Category> findAll();

    /**
     * 根据 rid找 category
     */
    Category findOne(String rid);
}
