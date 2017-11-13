package com.depromeet.todo.controller.page;

import com.depromeet.todo.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TodoController {

    @GetMapping("/")
    public String handleTodoPage(@AuthenticationPrincipal User user) {
        return user == null ? "splash" : "todo_list";
    }

    @GetMapping("/todos/{todo_id}")
    public String handleTodoDetailPage(@PathVariable("todo_id") Long todoId, Model model) {

        model.addAttribute("todoId", todoId);
        return "todo_detail";
    }
}
