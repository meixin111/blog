package blog.controller;

import blog.dto.CategoryDTO;
import blog.dto.ResponseResult;
import blog.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/list")
    public ResponseResult<Map<String, Object>> list() {
        return categoryService.list();
    }

    @GetMapping("/detail")
    public ResponseResult<CategoryDTO> detail(@RequestParam Long id) {
        return categoryService.detail(id);
    }

    @PostMapping("/_token/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseResult<CategoryDTO> add(@Valid @RequestBody CategoryDTO dto) {
        return categoryService.add(dto);
    }

    @PutMapping("/_token/update")
    @PreAuthorize("isAuthenticated()")
    public ResponseResult<CategoryDTO> update(@Valid @RequestBody CategoryDTO dto) {
        return categoryService.update(dto);
    }

    @DeleteMapping("/_token/delete")
    @PreAuthorize("isAuthenticated()")
    public ResponseResult<Void> delete(@RequestParam Long id) {
        return categoryService.delete(id);
    }
}