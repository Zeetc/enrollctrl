package com.catchiz.enrollctrl.mapper;

import com.catchiz.enrollctrl.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select count(username) from user where username = #{username}")
    int hasSameUsername(String username);

    @Insert("insert into user values(#{username},#{password},#{email},#{gender},#{isManager},#{departmentId},#{registerDate},#{describe})")
    void register(User user);

    @Delete("delete from user where username = #{username}")
    void delUser(String username);

    @Select("select email from user where username = #{username}")
    String getEmailByUsername(String username);

    @Update("update from user set password = #{password} where username = #{username}")
    void resetPassword(@Param("username") String username, @Param("password") String password);

    @Select("select * from user where username = #{username}")
    User getUserByUsername(String username);

    @Select("select * from user")
    List<User> listAllUser();
}
