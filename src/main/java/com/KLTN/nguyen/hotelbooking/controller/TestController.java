package com.KLTN.nguyen.hotelbooking.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {
    @GetMapping("/public")
    public String he(){
        var authen = SecurityContextHolder.getContext().getAuthentication();
        log.info(authen.getName());
        authen.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        return "a";
    }
}
