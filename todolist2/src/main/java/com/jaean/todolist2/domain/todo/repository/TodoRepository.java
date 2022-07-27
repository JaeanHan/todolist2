package com.jaean.todolist2.domain.todo.repository;

import com.jaean.todolist2.domain.todo.entity.Todo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TodoRepository {
    public int save(Todo todo) throws Exception;

    public List<Todo> getTodoListOfIndex(Map<String, Object> map) throws Exception;

    public List<Todo> getImportanceTodoListOfIndex(Map<String, Object> map) throws Exception;

    public int updateTodoComplete(int todo_code) throws Exception;
    public int updateTodoImportance(int todo_code) throws Exception;
    public int updateTodoByTodoCode(Todo todo) throws Exception;

    public int remove(int todo_code) throws Exception;
}
