package org.webp.intro.exercises.quizgame.frontend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.webp.intro.exercises.quizgame.backend.entity.MatchStats;
import org.webp.intro.exercises.quizgame.backend.service.MatchStatsService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class UserInfoController {

    @Autowired
    private MatchStatsService matchStatsService;

    public String getUserName(){
        return ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

    public MatchStats getStats(){
        return matchStatsService.getMatchStats(getUserName());
    }
}
