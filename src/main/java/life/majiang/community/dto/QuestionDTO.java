package life.majiang.community.dto;

import life.majiang.community.model.User;
import lombok.Data;

/**
 * @author a123
 * question的运输层
 */
@Data
public class QuestionDTO {
    private Integer id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User user;
}
