package life.majiang.community.dto;

import lombok.Data;

/**
 * @author a123
 */
@Data
public class GithubUser {
    private String name;
    private long id;
    private String bio;
    private String avatar_url;
}
