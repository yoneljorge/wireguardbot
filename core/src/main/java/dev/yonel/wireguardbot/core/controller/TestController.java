package dev.yonel.wireguardbot.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.yonel.wireguardbot.common.dtos.UserDto;
import dev.yonel.wireguardbot.common.services.UserService;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserService userService;

    @PostMapping
    public void setUser(@RequestBody UserDto user){
        userService.createUser(user);
    }

    @GetMapping
    public String getHealth(){
        return "Esto esta ok";
    }
}
