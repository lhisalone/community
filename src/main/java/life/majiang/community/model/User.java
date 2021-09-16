package life.majiang.community.model;

import lombok.Data;

/**
 * @author a123
 */
@Data
public class User {

    private Integer id;
    private String name;
    private String accountId;
    private String token;
    private long gmtCreate;
    private long gmtModified;
}
