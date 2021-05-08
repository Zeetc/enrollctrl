package com.catchiz.enrollctrl.pojo;

import lombok.Data;

import java.util.Objects;


@Data
public class AnswerAuthor {
    private Integer authorId;
    private String authorName;
    private String authorEmail;
    private Integer isPass;
    private Integer questionnaireId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnswerAuthor that = (AnswerAuthor) o;

        if (!Objects.equals(authorName, that.authorName)) return false;
        if (!Objects.equals(authorEmail, that.authorEmail)) return false;
        return Objects.equals(isPass, that.isPass);
    }

    @Override
    public int hashCode() {
        int result = authorName != null ? authorName.hashCode() : 0;
        result = 31 * result + (authorEmail != null ? authorEmail.hashCode() : 0);
        result = 31 * result + (isPass != null ? isPass.hashCode() : 0);
        return result;
    }
}
