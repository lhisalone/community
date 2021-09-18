package life.majiang.community.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author a123
 * 对应数据库的表头
 */
@Data
@Accessors(chain = true)
public class User {

    private Integer id;
    private String name;
    private String accountId;
    private String token;
    private long gmtCreate;
    private long gmtModified;
    private String avatarUrl;
}
