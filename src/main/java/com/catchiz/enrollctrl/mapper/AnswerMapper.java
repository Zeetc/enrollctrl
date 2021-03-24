package com.catchiz.enrollctrl.mapper;

import com.catchiz.enrollctrl.pojo.Answer;
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
}
