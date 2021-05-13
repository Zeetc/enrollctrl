package com.catchiz.enrollctrl.pojo;

import lombok.Data;

import java.util.List;

@Data
public class Article {
    private String title;
    private Integer departmentId;
    private String text;
    private List<String> imagesList;
    private String imagesJson;
}
