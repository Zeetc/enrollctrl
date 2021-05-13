package com.catchiz.enrollctrl.controller;

import com.catchiz.enrollctrl.pojo.Article;
import com.catchiz.enrollctrl.pojo.CommonResult;
import com.catchiz.enrollctrl.pojo.CommonStatus;
import com.catchiz.enrollctrl.pojo.User;
import com.catchiz.enrollctrl.service.ArticleService;
import com.catchiz.enrollctrl.service.UserService;
import com.catchiz.enrollctrl.utils.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/article")
public class ArticleController {
    private final UserService userService;
    private final ArticleService articleService;

    public ArticleController(UserService userService, ArticleService articleService) {
        this.userService = userService;
        this.articleService = articleService;
    }

    @PostMapping("/modifyArticle")
    @ApiOperation("新建或覆盖原article")
    public CommonResult modifyArticle(Article article, @RequestHeader String Authorization){
        String username = JwtTokenUtil.getUsernameFromToken(Authorization);
        if(!StringUtils.hasText(username))return new CommonResult(CommonStatus.FORBIDDEN,"输入不合法");
        User user = userService.getUserByUsername(username);
        articleService.modifyArticle(article,user.getDepartmentId());
        return new CommonResult(CommonStatus.OK,"上传成功");
    }
}
