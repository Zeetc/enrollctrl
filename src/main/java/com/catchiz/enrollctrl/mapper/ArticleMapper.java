package com.catchiz.enrollctrl.mapper;

import com.catchiz.enrollctrl.pojo.Article;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.mapstruct.Mapper;

@Mapper
public interface ArticleMapper {
    @Select("select * from article where department_id = #{departmentId}")
    Article getArticleByDepartmentId(Integer departmentId);

    @Select("select count(*) from article where department_id = #{departmentId}")
    int checkHaveArticle(Integer departmentId);

    @Insert("insert into article values(#{departmentId},#{text},#{title},#{imageJson})")
    void insertArticle(Article article);

    @Update("update article set title = #{title},text = #{text},imagesJson=#{imagesJson} where department_id = #{departmentId}")
    void updateArticle(Article article);
}
