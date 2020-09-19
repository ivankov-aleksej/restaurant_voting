package com.example.restaurant_voting.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
public class LoggingConfig {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(public * com.example.restaurant_voting.web.rest.RestaurantController.*(..)) " +
            "&& !@annotation(com.example.restaurant_voting.config.NoLogging)")
    public void callRestaurantController() {
        //NOP
    }

    @Pointcut("execution(public * com.example.restaurant_voting.web.rest.MenuController.*(..)) " +
            "&& !@annotation(com.example.restaurant_voting.config.NoLogging)")
    public void callMenuController() {
        //NOP
    }

    @Pointcut("execution(public * com.example.restaurant_voting.web.rest.DishController.*(..))  " +
            "&& !@annotation(com.example.restaurant_voting.config.NoLogging)")
    public void callDishController() {
        //NOP
    }

    @Pointcut("execution(public * com.example.restaurant_voting.web.rest.VoteController.*(..))  " +
            "&& !@annotation(com.example.restaurant_voting.config.NoLogging)")
    public void callVoteController() {
        //NOP
    }

    @Before("callRestaurantController()")
    public void beforeCallRestaurantControllerMethod(JoinPoint jp) {
        logging(jp);
    }

    @Before("callMenuController()")
    public void beforeCallMenuControllerMethod(JoinPoint jp) {
        logging(jp);
    }

    @Before("callDishController()")
    public void beforeCallDishControllerMethod(JoinPoint jp) {
        logging(jp);
    }

    @Before("callVoteController()")
    public void beforeCallVoteControllerMethod(JoinPoint jp) {
        logging(jp);
    }

    private void logging(JoinPoint jp) {
        String args = Arrays.stream(jp.getArgs())
                .map(Object::toString)
                .collect(Collectors.joining(","));
        logger.info("{}, args=[{}]", jp, args);
    }
}
