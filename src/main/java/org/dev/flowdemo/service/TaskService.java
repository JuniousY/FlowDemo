package org.dev.flowdemo.service;

import org.dev.flowdemo.dto.TaskDTO;
import org.dev.flowdemo.dto.req.TaskQueryReq;
import org.dev.flowdemo.dto.resp.ServiceResp;

import java.util.List;

public interface TaskService {

    ServiceResp<TaskDTO> createTask(TaskDTO dto);

    ServiceResp<TaskDTO> updateTask(TaskDTO dto);

    ServiceResp<TaskDTO> getTaskById(Long id);

    ServiceResp<List<TaskDTO>> queryTasks(TaskQueryReq req);
}
