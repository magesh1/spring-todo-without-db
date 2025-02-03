package com.example.TodoWODB.service;

import com.example.TodoWODB.model.Todo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class TodoService {
    private List<Todo> todoList = new ArrayList<>();
    private AtomicLong nextId = new AtomicLong(1);

    public TodoService() {
        System.out.println("populating data");
        prePopulate();
        System.out.println("populated data");
    }

    public Todo createTodo(Todo todo) {
        todo.setId(nextId.getAndIncrement());
        todoList.add(todo);
        return todo;
    }

    public List<Todo> listTodo(String sortby, String orderby, Long id) {
        if (id != null) {
            Todo res = getTodoById(id);
            if (res == null) {
                return Collections.emptyList();
            }
            return Collections.singletonList(res);
        }
        Comparator<Todo> comparator = (todo1, todo2) -> {
            switch (sortby.toLowerCase()) {
                case "title":
                    return todo1.getTitle().compareTo(todo2.getTitle());
                case "completed":
                    return Boolean.compare(todo1.isCompleted(), todo2.isCompleted());
                default:
                    return Long.compare(todo1.getId(), todo2.getId());
            }
        };


        if ("desc".equalsIgnoreCase(orderby)) {
            comparator = comparator.reversed();
        }

        return todoList.stream().sorted(comparator).collect(Collectors.toList());
    }

    public String deleteTodo(Long id) {
        Todo val = getTodoById(id);
        String msg = "success";

        if (val == null) {
            msg = id + " is not found in the db";
            return msg;
        }

        todoList.remove(val);
        return msg;
    }

    private void prePopulate() {
        String[] task = {"golang", "java", "learn code", "prepare interview", "spring boot"};
        for (int i = 0; i < 5; i++) {
            Todo todo = new Todo();
            todo.setId(nextId.getAndIncrement());
            todo.setTitle(task[i]);
            todo.setCompleted(Math.random() < 0.5);
            todoList.add(todo);
        }


    }


    public String updateTask(Todo todo) {
        Todo val = getTodoById(todo.getId());

        if (val == null) {
            return todo.getId() + " id not found";
        }

        if (todo.getTitle() != null) {
            if (todo.getTitle() != " ") {
                val.setTitle(todo.getTitle());
            }
        }

        if (val.isCompleted() != todo.isCompleted()) {
            val.setCompleted(todo.isCompleted());
        }


        return todo.getId() + " is updated successfully";
    }

    private Todo getTodoById(long id) {
        Todo result = null;
        for (Todo todo : todoList) {
            if (todo.getId() == id) {
                result = todo;
                break;
            }
        }
        return result;
    }

}
