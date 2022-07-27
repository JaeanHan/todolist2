package com.jaean.todolist2.domain.todo.entity;

import com.jaean.todolist2.web.dto.todo.TodoListRespDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Todo {
    private int todo_code;
    private String todo_content;
    private int todo_complete;
    private int importance_flag;
    private int total_count;
    private LocalDateTime create_date;
    private LocalDateTime update_date;

    public TodoListRespDto toListDto() {
        return TodoListRespDto.builder()
                .todoCode(todo_code)
                .todo(todo_content)
                .todoComplete(todo_complete == 1)
                .importance(importance_flag)
                .totalCount(total_count)
                .createDate(create_date)
                .updateDate(update_date)
                .build();
    }
}
