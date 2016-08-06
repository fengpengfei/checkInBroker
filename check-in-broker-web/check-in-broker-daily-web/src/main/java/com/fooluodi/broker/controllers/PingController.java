package com.fooluodi.broker.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by di on 6/8/2016.
 */
@RestController(value = "/")
public class PingController {
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public String ping() {
        return "hi!";
    }
}
