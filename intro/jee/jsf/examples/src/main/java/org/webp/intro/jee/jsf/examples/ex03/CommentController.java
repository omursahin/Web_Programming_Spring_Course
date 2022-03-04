package org.webp.intro.jee.jsf.examples.ex03;


import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.List;

@Named // JSF xhtml dosyalası tarafından erişilebilmek için gerekir
@RequestScoped //state tutmaya gerek yok yalnızca POST isteği için gerekli
public class CommentController {

    private String formText;

    @EJB
    private CommentEJB ejb;

    public void createNewComment() {
        ejb.createNewComment(formText);
        formText = "";
    }

    public List<Comment> getMostRecentComments(int max){
        return ejb.getMostRecentComments(max);
    }

    public void deleteComment(Long id){
        ejb.deleteComment(id);
    }

    public String getFormText() {
        return formText;
    }

    public void setFormText(String formText) {
        this.formText = formText;
    }
}
