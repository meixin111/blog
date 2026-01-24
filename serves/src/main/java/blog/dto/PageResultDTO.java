package blog.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageResultDTO<T> {
    private List<T> rows;
    private Long total;
    private Integer page;
    private Integer pageSize;

    public PageResultDTO() {}

    public PageResultDTO(List<T> rows, Long total, Integer page, Integer pageSize) {
        this.rows = rows;
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
    }
}