/**
 * 用户VO
 * 跟UserDO的区别是，UserVO包含了UserDO中的所有字段 和JWT Token
 */
package cc.rockbot.dds.dto;

import cc.rockbot.dds.model.UserDO;
import lombok.Data;

@Data
public class UserVO {
    private UserDO user;
    private String token;
}