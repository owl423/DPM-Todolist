package com.depromeet.todo.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    @GetMapping("/splash")
    public String splash() {
        return "splash";
    }
}
