package org.dev.flowdemo.service;

import org.dev.flowdemo.constants.ResponseCode;
import org.dev.flowdemo.constants.TaskConstants;
import org.dev.flowdemo.dto.TaskDTO;
import org.dev.flowdemo.dto.req.TaskQueryReq;
import org.dev.flowdemo.dto.resp.ServiceResp;
import org.dev.flowdemo.mapper.EmployeeMapper;
import org.dev.flowdemo.mapper.TaskMapper;
import org.dev.flowdemo.model.Employee;
import org.dev.flowdemo.model.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private LockExecutor lockExecutor;

    @Autowired
    private TaskServiceExecutor taskServiceExecutor;

    @Override
    public ServiceResp<TaskDTO> createTask(TaskDTO dto) {
        if (!StringUtils.hasText(dto.getReqId())) {
            return ServiceResp.fail(ResponseCode.BAD_REQUEST.getCode(), "请求 ID 不能为空");
        }
        String lockKey = "task:create:" + dto.getReqId();

        try {
            ServiceResp<TaskDTO> resp = lockExecutor.executeWithLock(lockKey, 500, 1000, TimeUnit.MILLISECONDS,
                    () -> {
                        ServiceResp<Task> createResp = taskServiceExecutor.createTaskHandler(dto);
                        return getTaskDTOServiceResp(createResp);
                    });
            taskServiceExecutor.evictProjectTasks(dto.getProjectId());
            return resp;
        } catch (Exception e) {
            return ServiceResp.fail(ResponseCode.BUSINESS_ERROR.getCode(), e.getMessage());
        }
    }

    private ServiceResp<TaskDTO> getTaskDTOServiceResp(ServiceResp<Task> resp) {
        if (resp.getCode() == ResponseCode.SUCCESS.getCode()) {
            TaskDTO taskDTO = new TaskDTO();
            BeanUtils.copyProperties(resp.getData(), taskDTO);
            genTaskDTOInfos(taskDTO);
            return ServiceResp.success(taskDTO);
        } else {
            return new ServiceResp<>(resp.getCode(), resp.getMessage(), null);
        }
    }

    @Override
    public ServiceResp<TaskDTO> updateTask(TaskDTO dto) {
        if (!StringUtils.hasText(dto.getReqId())) {
            return ServiceResp.fail(ResponseCode.BAD_REQUEST.getCode(), "请求 ID 不能为空");
        }
        if (dto.getTaskId() == null) {
            return ServiceResp.fail(ResponseCode.BAD_REQUEST.getCode(), "任务 ID 不能为空");
        }
        if (dto.getStatus() == null) {
            return ServiceResp.fail(ResponseCode.BAD_REQUEST.getCode(), "任务 状态 不能为空");
        }
        String lockKey = "task:update:" + dto.getReqId();

        try {
            ServiceResp<Task> resp = lockExecutor.executeWithLock(lockKey, 500, 1000, TimeUnit.MILLISECONDS,
                    () -> taskServiceExecutor.updateTaskHandler(dto));
            taskServiceExecutor.evictProjectTasks(dto.getProjectId());
            return getTaskDTOServiceResp(resp);
        } catch (Exception e) {
            return ServiceResp.fail(ResponseCode.BUSINESS_ERROR.getCode(), e.getMessage());
        }
    }


    private void genTaskDTOInfos(TaskDTO task) {
        // 设置状态描述
        task.setStatusDesc(TaskConstants.TaskStatus.getInstance(task.getStatus()).getDescription());

        // 如果有负责人ID，查询负责人姓名
        if (task.getAssigneeId() != null) {
            Employee assignee = employeeMapper.selectById(task.getAssigneeId());
            if (assignee != null) {
                task.setAssigneeName(assignee.getName());
            }
        }
    }


    @Override
    public ServiceResp<TaskDTO> getTaskById(Long id) {
        if (id == null) {
            return ServiceResp.fail(ResponseCode.BAD_REQUEST.getCode(), "任务ID不能为空");
        }
        Task task = taskMapper.selectById(id);
        if (task == null) {
            return ServiceResp.fail(ResponseCode.NOT_FOUND.getCode(), "任务不存在");
        }
        TaskDTO dto = new TaskDTO();
        BeanUtils.copyProperties(task, dto);
        return ServiceResp.success(dto);
    }

    @Override
    public ServiceResp<List<TaskDTO>> queryTasks(TaskQueryReq req) {
        if (req.getProjectId() == null) {
            return ServiceResp.fail(ResponseCode.BAD_REQUEST.getCode(), "项目 ID 不能为空");
        }

        // 缓存查询
        List<TaskDTO> cached = taskServiceExecutor.getProjectCachedTasks(req.getProjectId());
        if (cached != null) {
            return ServiceResp.success(cached);
        }

        // 数据库查询
        List<Task> tasks = taskMapper.queryTasks(req);
        List<TaskDTO> dtos = tasks.stream().map(task -> {
            TaskDTO dto = new TaskDTO();
            BeanUtils.copyProperties(task, dto);
            genTaskDTOInfos(dto);
            return dto;
        }).collect(Collectors.toList());

        // 缓存结果
        taskServiceExecutor.cacheProjectTasks(req.getProjectId(), dtos);

        return ServiceResp.success(dtos);
    }
}
