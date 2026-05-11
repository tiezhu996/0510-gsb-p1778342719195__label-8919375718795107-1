package com.fruitshop.service;

import com.fruitshop.common.PageResult;
import com.fruitshop.vo.FruitVO;

import java.util.Map;

public interface FruitService {

    Map<String, Object> getHomeData();

    PageResult<FruitVO> getFruitList(Long categoryId, String keyword, String sort, int page, int size);

    FruitVO getFruitDetail(Long id);
}
