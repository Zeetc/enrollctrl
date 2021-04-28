package com.catchiz.enrollctrl.mapper;

import com.catchiz.enrollctrl.pojo.Questionnaire;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface QuestionnaireMapper {
    @Insert("insert into questionnaire values(#{id},#{departmentId},#{title},#{describe})")
    @Options(useGeneratedKeys = true,keyColumn = "id",keyProperty = "id")
    void insertQuestionnaire(Questionnaire questionnaire);

    @Select("select * from questionnaire where id = #{questionnaireId}")
    Questionnaire getQuestionnaireByQuestionnaireId(Integer questionnaireId);
}
