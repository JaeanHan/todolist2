package com.jaean.todolist2.web.dto.todo;

import com.jaean.todolist2.domain.todo.entity.Todo;
import lombok.Data;

@Data
public class UpdateTodoReqDto {
    private int todoCode;
    private String todo;

    public Todo toEntity() {
        return Todo.builder()
                .todo_code(todoCode)
                .todo_content(todo)
                .build();
    }
}
