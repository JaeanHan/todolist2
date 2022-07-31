package com.jaean.todolist2.service.todo;

import com.jaean.todolist2.domain.todo.entity.Todo;
import com.jaean.todolist2.domain.todo.repository.TodoRepository;
import com.jaean.todolist2.web.dto.todo.CreateTodoReqDto;
import com.jaean.todolist2.web.dto.todo.TodoListRespDto;
import com.jaean.todolist2.web.dto.todo.UpdateTodoReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService{
    private final TodoRepository todoRepository;
    @Override
    public boolean createTodo(CreateTodoReqDto createTodoReqDto) throws Exception {
        Todo todoEntity = createTodoReqDto.toEntity();
        String content = todoEntity.getTodo_content();

//        for(int i = 100; i < 200; i++) { 200개 추가함
//            todoEntity.setTodo_content(content + "_" + (i + 1));
//
//            if(i % 2 == 0) {
//                todoEntity.setImportance_flag(1);
//            } else {
//                todoEntity.setImportance_flag(0);
//            }
//            todoRepository.save(todoEntity);
//        }

        return todoRepository.save(todoEntity) > 0;
    }

    @Override
    public List<TodoListRespDto> getTodoList(String type, int page, int contentCount) throws Exception {
        return createTodoListRespDtos(createGetTodoListMap(type, page, contentCount));
    }

    @Override
    public boolean updateTodoComplete(int todoCode) throws Exception {
        return todoRepository.updateTodoComplete(todoCode) > 0;
    }

    @Override
    public boolean updateTodoImportance(int todoCode) throws Exception {
        return todoRepository.updateTodoImportance(todoCode) > 0;
    }

    @Override
    public boolean updateTodo(UpdateTodoReqDto updateTodoReqDto) throws Exception {
        return todoRepository.updateTodoByTodoCode(updateTodoReqDto.toEntity()) > 0;
    }

    @Override
    public boolean removeTodo(int todoCode) throws Exception {
        return todoRepository.remove(todoCode) > 0;
    }

    private Map<String, Object> createGetTodoListMap(String type, int page, int contentCount) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("index", (page - 1) * contentCount);
        map.put("count", contentCount);
        return map;
    }

    private List<TodoListRespDto> createTodoListRespDtos(Map<String, Object> map) throws Exception {
        List<TodoListRespDto> todoListRespDtos = new ArrayList<>();
        todoRepository.getTodoListOfIndex(map).forEach(todo -> todoListRespDtos.add(todo.toListDto()));
        return todoListRespDtos;
    }
}
