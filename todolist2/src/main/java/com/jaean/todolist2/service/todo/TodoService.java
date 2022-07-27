package com.jaean.todolist2.service.todo;

import com.jaean.todolist2.web.dto.todo.CreateTodoReqDto;
import com.jaean.todolist2.web.dto.todo.SelectTodoListReqDto;
import com.jaean.todolist2.web.dto.todo.TodoListRespDto;
import com.jaean.todolist2.web.dto.todo.UpdateTodoReqDto;

import java.util.List;

public interface TodoService {
    // 추가
    public boolean createTodo(CreateTodoReqDto createTodoReqDto) throws Exception;

    // 조회
    public List<TodoListRespDto> getTodoList(int page, int contentCount) throws Exception;
    public List<TodoListRespDto> getImportanceTodoList(SelectTodoListReqDto selectTodoListReqDto) throws Exception;

    // 수정
    public boolean updateTodoComplete(int todoCode) throws Exception;
    public boolean updateTodoImportance(int todoCode) throws Exception;
    public boolean updateTodo(UpdateTodoReqDto updateTodoReqDto) throws Exception;

    // 삭제
    public boolean removeTodo(int todoCode) throws Exception;
}
