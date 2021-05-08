package com.catchiz.enrollctrl.mapper;

import com.catchiz.enrollctrl.pojo.AnswerAuthor;
import org.apache.ibatis.annotations.*;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface AnswerAuthorMapper {

    @Insert("insert into answer_author values(#{authorId},#{authorName},#{authorEmail},#{isPass},#{questionnaireId})")
    @Options(useGeneratedKeys = true,keyColumn = "author_id",keyProperty = "authorId")
    void saveAuthor(AnswerAuthor author);

    @Select("select * from answer_author where author_id = #{id}")
    AnswerAuthor getAuthorById(Integer id);

    @Update("update answer_author set author_name = #{authorName} where author_id = #{authorId}")
    void updateAnswerAuthorName(@Param("authorId") Integer authorId, @Param("authorName") String authorName);

    @Update("update answer_author set author_email = #{email} where author_id = #{authorId}")
    void updateAnswerAuthorEmail(@Param("authorId") Integer authorId, @Param("email") String email);

    @Update("update answer_author set is_pass = #{isPass} where author_id = #{authorId}")
    void setIsPass(@Param("authorId") Integer authorId,@Param("isPass")Integer isPass);

    @Select("select * from answer_author where questionnaireId = #{questionnaireId} and is_pass = 1")
    List<AnswerAuthor> getAllUserIsPass(Integer questionnaireId);

    @Select("select * from answer_author where questionnaireId = #{questionnaireId}")
    List<AnswerAuthor> getAllUserByQuestionnaireId(Integer questionnaireId);

    @Delete("delete from answer_author where questionnaireId = #{questionnaireId}")
    void deleteQuestionnaire(Integer questionnaireId);

    @Delete("delete from answer_author where author_id = #{author_id}")
    void deleteAuthor(Integer authorId);
}
