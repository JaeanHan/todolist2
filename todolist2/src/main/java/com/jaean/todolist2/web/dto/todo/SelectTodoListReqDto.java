package com.jaean.todolist2.web.dto.todo;

import lombok.Data;

@Data
public class SelectTodoListReqDto {
    private int page;
    private int contentCount;
    private int importance;
}
