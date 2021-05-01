package com.catchiz.enrollctrl.mapper;

import com.catchiz.enrollctrl.pojo.Answer;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AnswerMapper {
    @Insert("insert into answer values(#{problemId},#{jsonVal},#{author})")
    void insertAnswer(Answer answer);

    @Select("select * from answer where problemId = #{problemId}")
    List<Answer> getAnsByProblemId(Integer id);

    @Select("select author_id from answer where questionnaire_id = #{questionnaireId}")
    List<Integer> getAllAnswerAuthor(Integer questionnaireId);

    @Select("select * from answer where author_id = #{answerAuthorId}")
    List<Answer> getAnswerByAuthor(Integer answerAuthorId);

    @Delete("delete from answer where questionnaire_id = #{questionnaireId}")
    void deleteQuestionnaire(Integer questionnaireId);

    @Delete("delete from answer where author_id = #{authorId}")
    void deleteAuthor(Integer authorId);
}
