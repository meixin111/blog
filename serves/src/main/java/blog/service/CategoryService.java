package blog.service;

import blog.dto.CategoryDTO;
import blog.dto.ResponseResult;
import blog.entity.Category;
import blog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public ResponseResult<CategoryDTO> add(CategoryDTO dto) {
        if (categoryRepository.existsByName(dto.getName())) {
            return ResponseResult.error(400, "分类名称已存在");
        }

        Category category = new Category();
        category.setName(dto.getName());
        categoryRepository.save(category);

        CategoryDTO result = new CategoryDTO();
        result.setId(category.getId());
        result.setName(category.getName());

        return ResponseResult.success("添加成功", result);
    }

    public ResponseResult<CategoryDTO> update(CategoryDTO dto) {
        Category category = categoryRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("分类不存在"));

        categoryRepository.findByName(dto.getName())
                .ifPresent(c -> {
                    if (!c.getId().equals(dto.getId())) {
                        throw new RuntimeException("分类名称已存在");
                    }
                });

        category.setName(dto.getName());
        categoryRepository.save(category);

        return ResponseResult.success("更新成功", dto);
    }

    public ResponseResult<Void> delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));

        categoryRepository.delete(category);
        return ResponseResult.success("删除成功", null);
    }

    public ResponseResult<Map<String, Object>> list() {
        List<Category> categories = categoryRepository.findAllByOrderByCreateTimeDesc();
        List<Map<String, Object>> rows = categories.stream()
                .map(category -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", category.getId());
                    map.put("name", category.getName());
                    return map;
                })
                .collect(Collectors.toList());

        // 创建前端期望的数据结构
        Map<String, Object> data = new HashMap<>();
        data.put("rows", rows);
        data.put("count", (long) rows.size());

        return ResponseResult.success(data);
    }

    public ResponseResult<CategoryDTO> detail(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));

        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());

        return ResponseResult.success(dto);
    }
}