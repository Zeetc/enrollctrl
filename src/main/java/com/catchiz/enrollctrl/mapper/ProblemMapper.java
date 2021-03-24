package com.catchiz.enrollctrl.mapper;

import com.catchiz.enrollctrl.pojo.Problem;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProblemMapper {
    @Insert("insert into problem values(#{id},#{title},#{type},#{jsonVal},#{index},#{questionnaireId})")
    void insertProblem(Problem problem);

    @Select("select * from question where questionnaireId = #{questionnaireId}")
    List<Problem> listProblemsByQuestionnaireId(Integer questionnaireId);

    @Select("select * from problem where id = #{problemId}")
    Problem getProblemByProblemId(Integer problemId);

    @Select("select * from question where questionnaireId = #{questionnaireId}")
    List<Problem> getProblemIdsByQuestionnaireId(Integer questionnaireId);
}
