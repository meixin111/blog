package blog.service;

import blog.dto.*;
import blog.entity.Article;
import blog.entity.Category;
import blog.entity.User;
import blog.repository.ArticleRepository;
import blog.repository.CategoryRepository;
import blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public ResponseResult<Map<String, Object>> search(SearchDTO dto) {
        Pageable pageable = PageRequest.of(
                dto.getPage() - 1,
                dto.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createTime")
        );

        Page<Article> page = articleRepository.searchArticles(
                dto.getKeyword(),
                dto.getCategoryId(),
                pageable
        );

        List<Map<String, Object>> rows = page.getContent().stream()
                .map(this::convertToMap)
                .collect(Collectors.toList());

        // 创建前端期望的数据结构
        Map<String, Object> data = new HashMap<>();
        data.put("rows", rows);
        data.put("count", page.getTotalElements());
        // 添加前端可能需要的其他字段
        data.put("total", page.getTotalElements());

        return ResponseResult.success(data);
    }

    public ResponseResult<Map<String, Object>> detail(Long id) {
    Article article = articleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("文章不存在"));

    // ========== 开始修改 ==========
    // 原代码（错误）：return ResponseResult.success(convertToMap(article));
    
    // 新代码（正确）：
    Map<String, Object> data = new HashMap<>();
    List<Map<String, Object>> rows = new ArrayList<>();
    rows.add(convertToMap(article));
    data.put("rows", rows);
    
    return ResponseResult.success(data);
    // ========== 修改结束 ==========
}

    public ResponseResult<ArticleDTO> add(ArticleDTO dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("分类不存在"));

        Article article = new Article();
        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        article.setCategory(category);
        article.setUser(user);

        articleRepository.save(article);

        dto.setId(article.getId());
        return ResponseResult.success("添加成功", dto);
    }

    public ResponseResult<ArticleDTO> update(ArticleDTO dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        Article article = articleRepository.findByIdAndUserId(dto.getId(), user.getId())
                .orElseThrow(() -> new RuntimeException("文章不存在或无权限修改"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("分类不存在"));

        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        article.setCategory(category);

        articleRepository.save(article);

        return ResponseResult.success("更新成功", dto);
    }

    public ResponseResult<Void> delete(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        Article article = articleRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("文章不存在或无权限删除"));

        articleRepository.delete(article);
        return ResponseResult.success("删除成功", null);
    }

    private Map<String, Object> convertToMap(Article article) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", article.getId());
        map.put("title", article.getTitle());
        map.put("content", article.getContent());
        map.put("categoryId", article.getCategory() != null ? article.getCategory().getId() : null);
        map.put("create_time", article.getCreateTime()); // 保持前端使用的字段名
        return map;
    }
}