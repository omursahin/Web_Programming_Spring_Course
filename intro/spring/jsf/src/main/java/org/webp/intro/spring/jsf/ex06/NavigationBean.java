package org.webp.intro.spring.jsf.ex06;

import org.webp.intro.spring.jsf.ex04.SessionCounter;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class NavigationBean {

    @Inject
    private SessionCounter counter;

    public String increaseAndForward(){
        counter.increaseCounter();
        return "ex06_result.xhtml";
    }

    public String increaseAndRedirect(){
        counter.increaseCounter();
        return "ex06_result.xhtml?faces-redirect=true";
    }
}
