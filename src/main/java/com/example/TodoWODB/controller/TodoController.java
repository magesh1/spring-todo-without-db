package com.example.TodoWODB.controller;

import com.example.TodoWODB.model.Todo;
import com.example.TodoWODB.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TodoController {

    private TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping("/todo")
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
        Todo resp = todoService.createTodo(todo);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping("/todo")
    public ResponseEntity<?> listTodo( // ? will get dynamic response
                                       @RequestParam(value = "sort_by", defaultValue = "id") String sortby,
                                       @RequestParam(value = "order_by", defaultValue = "desc") String orderby,
                                       @RequestParam(value = "id", required = false) Long id) {
        List<Todo> resp = todoService.listTodo(sortby, orderby, id);

        if (resp.isEmpty()) {
            String msg = id + " is not found in the db";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg); // return string
        }

        return ResponseEntity.status(HttpStatus.OK).body(resp); // return List<Todo>
    }

    @DeleteMapping("/todo")
    public ResponseEntity<String> deleteTask(@RequestParam(value = "id") Long id) {
        String resp = todoService.deleteTodo(id);
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @PutMapping("/todo")
    public ResponseEntity<String> updateTask(@RequestBody Todo todo) {
        System.out.println(todo.toString());
        String resp = todoService.updateTask(todo);
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }


}
