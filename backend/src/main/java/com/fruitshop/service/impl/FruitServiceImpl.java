package com.fruitshop.service.impl;

import com.fruitshop.common.PageResult;
import com.fruitshop.common.ResultCode;
import com.fruitshop.entity.Category;
import com.fruitshop.entity.Fruit;
import com.fruitshop.entity.FruitSpec;
import com.fruitshop.exception.BusinessException;
import com.fruitshop.mapper.CategoryMapper;
import com.fruitshop.mapper.FruitMapper;
import com.fruitshop.mapper.FruitSpecMapper;
import com.fruitshop.service.FruitService;
import com.fruitshop.vo.FruitVO;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FruitServiceImpl implements FruitService {

    private final FruitMapper fruitMapper;
    private final FruitSpecMapper fruitSpecMapper;
    private final CategoryMapper categoryMapper;

    public FruitServiceImpl(FruitMapper fruitMapper, FruitSpecMapper fruitSpecMapper,
                            CategoryMapper categoryMapper) {
        this.fruitMapper = fruitMapper;
        this.fruitSpecMapper = fruitSpecMapper;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Map<String, Object> getHomeData() {
        Map<String, Object> result = new HashMap<>();

        // 轮播图
        List<Map<String, Object>> banners = new ArrayList<>();
        banners.add(createBanner(1L, "https://images.unsplash.com/photo-1610832958506-aa56368176cf?w=800&auto=format&fit=crop&q=80", "新鲜草莓 限时特惠", "/fruit/8"));
        banners.add(createBanner(2L, "https://images.unsplash.com/photo-1519996529931-28324d5a630e?w=800&auto=format&fit=crop&q=80", "智利车厘子 新鲜到货", "/fruit/1"));
        banners.add(createBanner(3L, "https://images.unsplash.com/photo-1546548970-71785318a17b?w=800&auto=format&fit=crop&q=80", "热带水果 产地直发", "/category/1"));
        result.put("banners", banners);

        // 分类
        List<Category> categories = categoryMapper.findAll();
        result.put("categories", categories);

        // 热门推荐
        List<Fruit> hotFruits = fruitMapper.findHotFruits(8);
        result.put("hotFruits", hotFruits.stream().map(FruitVO::fromFruit).collect(Collectors.toList()));

        // 新鲜上架
        List<Fruit> newFruits = fruitMapper.findNewFruits(8);
        result.put("newFruits", newFruits.stream().map(FruitVO::fromFruit).collect(Collectors.toList()));

        return result;
    }

    private Map<String, Object> createBanner(Long id, String image, String title, String link) {
        Map<String, Object> banner = new HashMap<>();
        banner.put("id", id);
        banner.put("image", image);
        banner.put("title", title);
        banner.put("link", link);
        return banner;
    }

    @Override
    public PageResult<FruitVO> getFruitList(Long categoryId, String keyword, String sort, int page, int size) {
        int offset = (page - 1) * size;
        List<Fruit> fruits = fruitMapper.findByCondition(categoryId, keyword, sort, offset, size);
        long total = fruitMapper.countByCondition(categoryId, keyword);

        List<FruitVO> fruitVOs = fruits.stream()
                .map(FruitVO::fromFruit)
                .collect(Collectors.toList());

        return PageResult.of(fruitVOs, total, page, size);
    }

    @Override
    public FruitVO getFruitDetail(Long id) {
        Fruit fruit = fruitMapper.findById(id);
        if (fruit == null) {
            throw new BusinessException(ResultCode.FRUIT_NOT_FOUND);
        }
        if (fruit.getStatus() == 0) {
            throw new BusinessException(ResultCode.FRUIT_OFF_SHELF);
        }

        // 获取规格
        List<FruitSpec> specs = fruitSpecMapper.findByFruitId(id);
        fruit.setSpecs(specs);

        return FruitVO.fromFruit(fruit);
    }
}
