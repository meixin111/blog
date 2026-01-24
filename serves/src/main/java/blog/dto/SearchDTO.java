package blog.dto;

import lombok.Data;

@Data
public class SearchDTO {
    private String keyword = "";
    private Long categoryId;
    private Integer page = 1;
    private Integer pageSize = 10;
}