package com.catchiz.enrollctrl.mapper;

import com.catchiz.enrollctrl.pojo.AnswerAuthor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.mapstruct.Mapper;

@Mapper
public interface AnswerAuthorMapper {

    @Insert("insert into answer_author values(#{authorId},#{authorName},#{authorEmail})")
    @Options(useGeneratedKeys = true,keyColumn = "author_id",keyProperty = "authorId")
    void saveAuthor(AnswerAuthor author);

    @Select("select * from answer_author where author_id = #{id}")
    AnswerAuthor getAuthorById(Integer id);

    @Update("update answer_author set author_name = #{authorName} where author_id = #{authorId}")
    void updateAnswerAuthorName(@Param("authorId") Integer authorId, @Param("authorName") String authorName);

    @Update("update answer_author set author_email = #{email} where author_id = #{authorId}")
    void updateAnswerAuthorEmail(@Param("authorId") Integer authorId, @Param("email") String email);
}
