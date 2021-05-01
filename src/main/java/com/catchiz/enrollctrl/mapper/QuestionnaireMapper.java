package com.catchiz.enrollctrl.mapper;

import com.catchiz.enrollctrl.pojo.Questionnaire;
import org.apache.ibatis.annotations.*;

@Mapper
public interface QuestionnaireMapper {
    @Insert("insert into questionnaire values(#{id},#{departmentId},#{title},#{describe})")
    @Options(useGeneratedKeys = true,keyColumn = "id",keyProperty = "id")
    void insertQuestionnaire(Questionnaire questionnaire);

    @Select("select * from questionnaire where id = #{questionnaireId}")
    Questionnaire getQuestionnaireByQuestionnaireId(Integer questionnaireId);

    @Delete("delete from questionnaire where id = #{questionnaireId}")
    void deleteQuestionnaire(Integer questionnaireId);
}
