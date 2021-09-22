package life.majiang.community.dto;

import lombok.Data;

/**
 * @author a123
 *
 */
@Data
public class GithubUser {
    /**
     * 从GitHub上返回的用户名
     */
    private String name;
    /**
     * 从GitHub上返回的用户ID
     */
    private Long id;
    private String bio;
    /**
     * 头像路径
     */
    private String avatar_url;
}
