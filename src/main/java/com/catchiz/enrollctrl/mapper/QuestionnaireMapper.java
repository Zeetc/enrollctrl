package com.catchiz.enrollctrl.mapper;

import com.catchiz.enrollctrl.pojo.Questionnaire;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;

@Mapper
public interface QuestionnaireMapper {
    @Insert("insert into questionnaire values(#{id},#{departmentId},#{title},#{describe},#{endDate})")
    @Options(useGeneratedKeys = true,keyColumn = "id",keyProperty = "id")
    void insertQuestionnaire(Questionnaire questionnaire);

    @Select("select * from questionnaire where id = #{questionnaireId}")
    Questionnaire getQuestionnaireByQuestionnaireId(Integer questionnaireId);

    @Delete("delete from questionnaire where id = #{questionnaireId}")
    void deleteQuestionnaire(Integer questionnaireId);

    @Update("update questionnaire set endDate = #{endDate} where id = #{questionnaireId}")
    void changeEndDate(@Param("questionnaireId") Integer questionnaireId, @Param("endDate") Timestamp endDate);
}
