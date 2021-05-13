package com.catchiz.enrollctrl.service;

import com.catchiz.enrollctrl.pojo.Article;

public interface ArticleService {
    Article getArticleByDepartmentId(Integer departmentId);

    void modifyArticle(Article article, Integer departmentId);
}
