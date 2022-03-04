package org.webp.intro.spring.jsf.ex03;


import org.springframework.beans.factory.annotation.Autowired;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.List;

@Named // JSF xhtml dosyaları tarafından erişilebilmesi için gerekli
@RequestScoped //state tutmaya gerek yok, yalnızca POST isteği için gerekli
public class CommentController {

    private String formText;

    @Autowired
    private CommentService service;

    public void createNewComment() {
        service.createNewComment(formText);
        formText = "";
    }

    public List<Comment> getMostRecentComments(int max) {
        return service.getMostRecentComments(max);
    }

    public void deleteComment(Long id) {
        service.deleteComment(id);
    }

    public String getFormText() {
        return formText;
    }

    public void setFormText(String formText) {
        this.formText = formText;
    }
}
