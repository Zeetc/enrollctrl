package com.catchiz.enrollctrl.pojo;

import lombok.Data;

import java.util.Objects;

@Data
public class AnswerAuthor {
    private Integer authorId;
    private String authorName;
    private String authorEmail;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnswerAuthor that = (AnswerAuthor) o;

        if (!Objects.equals(authorId, that.authorId)) return false;
        if (!Objects.equals(authorName, that.authorName)) return false;
        return Objects.equals(authorEmail, that.authorEmail);
    }

    @Override
    public int hashCode() {
        int result = authorId != null ? authorId.hashCode() : 0;
        result = 131 * result + (authorName != null ? authorName.hashCode() : 0);
        result = 131 * result + (authorEmail != null ? authorEmail.hashCode() : 0);
        return result;
    }
}
