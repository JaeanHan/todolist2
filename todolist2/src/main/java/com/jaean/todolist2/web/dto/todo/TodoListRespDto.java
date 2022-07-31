package com.jaean.todolist2.web.dto.todo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class TodoListRespDto {
    private int todoCode;
    private String todo;
    private boolean todoComplete;
    private int importance;
    private int totalCount;
    private int incompleteCount;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
