package blog.repository;

import blog.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findAll(Pageable pageable);

    Page<Article> findByTitleContaining(String keyword, Pageable pageable);

    Page<Article> findByCategoryId(Long categoryId, Pageable pageable);

    @Query("SELECT a FROM Article a WHERE " +
            "(COALESCE(:keyword, '') = '' OR a.title LIKE %:keyword%) AND " +
            "(:categoryId IS NULL OR a.category.id = :categoryId) " +
            "ORDER BY a.createTime DESC")
    Page<Article> searchArticles(
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId,
            Pageable pageable);

    List<Article> findByUserId(Long userId);

    Optional<Article> findByIdAndUserId(Long id, Long userId);
}