package com.catchiz.enrollctrl.service.impl;

import com.alibaba.fastjson.JSON;
import com.catchiz.enrollctrl.mapper.ArticleMapper;
import com.catchiz.enrollctrl.pojo.Article;
import com.catchiz.enrollctrl.service.ArticleService;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl implements ArticleService {
    private final ArticleMapper articleMapper;

    public ArticleServiceImpl(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    @Override
    public Article getArticleByDepartmentId(Integer departmentId) {
        Article article = articleMapper.getArticleByDepartmentId(departmentId);
        article.setImagesList(JSON.parseArray(article.getImagesJson(),String.class));
        return article;
    }

    @Override
    public void modifyArticle(Article article, Integer departmentId) {
        int count = articleMapper.checkHaveArticle(departmentId);
        if(count==0){
            article.setImagesJson(JSON.toJSONString(article.getImagesList()));
            articleMapper.insertArticle(article);
        }else {
            articleMapper.updateArticle(article);
        }
    }
}
