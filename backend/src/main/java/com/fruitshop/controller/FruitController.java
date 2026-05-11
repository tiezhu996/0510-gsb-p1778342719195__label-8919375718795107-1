package com.fruitshop.controller;

import com.fruitshop.common.PageResult;
import com.fruitshop.common.Result;
import com.fruitshop.service.FruitService;
import com.fruitshop.vo.FruitVO;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/fruit")
public class FruitController {

    private final FruitService fruitService;

    public FruitController(FruitService fruitService) {
        this.fruitService = fruitService;
    }

    @GetMapping("/home")
    public Result<Map<String, Object>> home() {
        Map<String, Object> data = fruitService.getHomeData();
        return Result.success(data);
    }

    @GetMapping("/list")
    public Result<PageResult<FruitVO>> list(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "default") String sort,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResult<FruitVO> pageResult = fruitService.getFruitList(categoryId, keyword, sort, page, size);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    public Result<FruitVO> detail(@PathVariable Long id) {
        FruitVO fruitVO = fruitService.getFruitDetail(id);
        return Result.success(fruitVO);
    }
}
