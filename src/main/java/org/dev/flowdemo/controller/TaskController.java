package org.dev.flowdemo.controller;

import lombok.RequiredArgsConstructor;
import org.dev.flowdemo.dto.TaskDTO;
import org.dev.flowdemo.dto.req.TaskQueryReq;
import org.dev.flowdemo.dto.resp.ServiceResp;
import org.dev.flowdemo.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task/v1")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/create")
    public ServiceResp<TaskDTO> createTask(@RequestBody TaskDTO dto) {
        return taskService.createTask(dto);
    }

    @PostMapping("/update")
    public ServiceResp<TaskDTO> updateTask(@RequestBody TaskDTO dto) {
        return taskService.updateTask(dto);
    }

    @GetMapping("/getById")
    public ServiceResp<TaskDTO> getTaskById(Long id) {
        return taskService.getTaskById(id);
    }

    @GetMapping("/query")
    public ServiceResp<List<TaskDTO>> queryTasks(TaskQueryReq req) {
        return taskService.queryTasks(req);
    }
}
