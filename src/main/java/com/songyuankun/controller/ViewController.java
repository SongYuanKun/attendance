package com.songyuankun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Administrator
 */
@Controller
@RequestMapping("")
public class ViewController {
    @RequestMapping("index")
    public String indexString() {
        return "index";
    }



}
