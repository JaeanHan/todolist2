package com.jaean.todolist2.web.api;

import com.jaean.todolist2.service.todo.TodoService;
import com.jaean.todolist2.web.dto.CMRespDto;
import com.jaean.todolist2.web.dto.todo.CreateTodoReqDto;
import com.jaean.todolist2.web.dto.todo.TodoListRespDto;
import com.jaean.todolist2.web.dto.todo.UpdateTodoReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/todolist")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @GetMapping("list/{type}")
    public ResponseEntity<?> getTodoList(@PathVariable String type, @RequestParam int page, @RequestParam int contentCount) {
        List<TodoListRespDto> list = null;
        try {
            list = todoService.getTodoList(type, page, contentCount);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new CMRespDto<>(-1, page + " page list load fail", null));
        }
        return ResponseEntity.ok().body(new CMRespDto<>(1, page + " page list load success", list));
    }

    @PostMapping("todo")
    public ResponseEntity<?> addTodo(@RequestBody CreateTodoReqDto createTodoReqDto) {
        try {
            if (! todoService.createTodo(createTodoReqDto)) {
                throw new RuntimeException("DataBase Error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new CMRespDto<>(-1, "Adding todo failed", null));
        }
        return ResponseEntity.ok().body(new CMRespDto<>(1, "success", createTodoReqDto));
    }

    @PutMapping("todo/complete/{todo_code}")
    public ResponseEntity<?> setCompleteTodo(@PathVariable int todo_code) {
        try {
            todoService.updateTodoComplete(todo_code);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new CMRespDto<>(-1, "Updating todo state failed", null));
        }
        return ResponseEntity.ok().body(new CMRespDto<>(1, "todo complete update success", todo_code));
    }

    @PutMapping("todo/importance/{todoCode}")
    public ResponseEntity<?> setImportanceTodo(@PathVariable int todoCode) {

        try {
            todoService.updateTodoImportance(todoCode);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new CMRespDto<>(-1, "Updating todo state failed", null));
        }
        return ResponseEntity.ok().body(new CMRespDto<>(1, "todo importance update success", todoCode));
    }

    @PutMapping("todo/{todoCode}")
    public ResponseEntity<?> setTodo(@PathVariable int todoCode, @RequestBody UpdateTodoReqDto updateTodoReqDto) {
        boolean status = false;
        try {
            updateTodoReqDto.setTodoCode(todoCode);
            status = todoService.updateTodo(updateTodoReqDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new CMRespDto<>(-1, "Updating todo failed", null));
        }
        return ResponseEntity.ok().body(new CMRespDto<>(1, "todo update success", status));
    }

    @DeleteMapping("todo/{todoCode}")
    public ResponseEntity<?> deleteTodo(@PathVariable int todoCode) {
        boolean status = false;

        try {
            status = todoService.removeTodo(todoCode);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new CMRespDto<>(-1, "Deleting todo failed", null));
        }
        return ResponseEntity.ok().body(new CMRespDto<>(1, "todo delete success", status));
    }


}
