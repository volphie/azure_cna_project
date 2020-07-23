package com.skcc.cna.sbtemplate.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class HelloController {

  @RequestMapping("/hello")
  public String hello(Model model, @RequestParam(value = "name", defaultValue = "Unknown", required = false) String name) {
    String greetings = "Hello, " + name + "!";
    model.addAttribute("greetings", greetings);
    return "hello";
  }
}