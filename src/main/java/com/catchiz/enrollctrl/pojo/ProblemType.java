package com.catchiz.enrollctrl.pojo;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class ProblemType {
    public static Set<String> typeSet=new HashSet<String>(){
        {add("text");}
        {add("radio");}
        {add("checkBox");}
        {add("multiple");}
    };
}
