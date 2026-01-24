package blog.controller;

import blog.dto.ArticleDTO;
import blog.dto.ResponseResult;
import blog.dto.SearchDTO;
import blog.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/search")
    public ResponseResult<Map<String, Object>> search(@Valid SearchDTO dto) {
        return articleService.search(dto);
    }

    @GetMapping("/detail")
    public ResponseResult<Map<String, Object>> detail(@RequestParam Long id) {
        return articleService.detail(id);
    }

    @PostMapping("/_token/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseResult<ArticleDTO> add(@Valid @RequestBody ArticleDTO dto) {
        return articleService.add(dto);
    }

    @PostMapping("/_token/update")
    @PreAuthorize("isAuthenticated()")
    public ResponseResult<ArticleDTO> update(@Valid @RequestBody ArticleDTO dto) {
        return articleService.update(dto);
    }

    @DeleteMapping("/_token/delete")
    @PreAuthorize("isAuthenticated()")
    public ResponseResult<Void> delete(@RequestParam Long id) {
        return articleService.delete(id);
    }
}