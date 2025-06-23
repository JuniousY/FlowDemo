package org.dev.flowdemo.service;

import jakarta.annotation.PostConstruct;
import org.dev.flowdemo.constants.ResponseCode;
import org.dev.flowdemo.constants.TaskConstants;
import org.dev.flowdemo.dto.TaskDTO;
import org.dev.flowdemo.dto.resp.ServiceResp;
import org.dev.flowdemo.mapper.ProjectMapper;
import org.dev.flowdemo.mapper.TaskLogMapper;
import org.dev.flowdemo.mapper.TaskMapper;
import org.dev.flowdemo.model.Project;
import org.dev.flowdemo.model.Task;
import org.dev.flowdemo.model.TaskLog;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;
import org.redisson.api.options.LocalCachedMapOptions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TaskServiceExecutor {

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private TaskLogMapper taskLogMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private RedissonClient redissonClient;

    private RLocalCachedMap<String, List<TaskDTO>> taskCache;

    @PostConstruct
    public void initCache() {
        LocalCachedMapOptions<String, List<TaskDTO>> options = LocalCachedMapOptions.<String, List<TaskDTO>>name("task_cache_by_project")
                .syncStrategy(LocalCachedMapOptions.SyncStrategy.INVALIDATE)
                .evictionPolicy(LocalCachedMapOptions.EvictionPolicy.LRU)
                .cacheSize(100)
                .timeToLive(Duration.of(10, ChronoUnit.MINUTES));
        taskCache = redissonClient.getLocalCachedMap(options);
    }

    @Transactional
    public ServiceResp<Task> createTaskHandler(TaskDTO dto) {
        Project project = projectMapper.selectById(dto.getProjectId());
        if (project == null) {
            return ServiceResp.fail(ResponseCode.BAD_REQUEST.getCode(), "项目不存在");
        }
        // todo employee permission check

        Task task = new Task();
        BeanUtils.copyProperties(dto, task);
        task.setStatus(TaskConstants.TaskStatus.NOT_STARTED.getCode());
        LocalDateTime now = LocalDateTime.now();
        task.setStartTime(now);
        task.setCreatedAt(now);
        task.setUpdatedAt(now);
        int inserted = taskMapper.insert(task);
        if (inserted == 0) {
            return ServiceResp.fail(ResponseCode.BUSINESS_ERROR);
        }
        dto.setTaskId(task.getId());

        TaskLog taskLog = new TaskLog();
        taskLog.setTaskId(task.getId());
        taskLog.setEmployeeId(dto.getOperator());
        taskLog.setAction(task.getStatus());
        taskLog.setCreatedAt(now);
        taskLogMapper.insert(taskLog);

        return ServiceResp.success(task);
    }

    @Transactional
    public ServiceResp<Task> updateTaskHandler(TaskDTO dto) {
        Task task = taskMapper.selectById(dto.getTaskId());
        if (task == null) {
            return ServiceResp.fail(ResponseCode.BAD_REQUEST.getCode(), "任务不存在");
        }
        // todo employee permission check

        if (dto.getVersion() != null && !dto.getVersion().equals(task.getVersion())) {
            return ServiceResp.fail(ResponseCode.BAD_REQUEST.getCode(), "数据已被修改，请刷新后重试");
        }

        LocalDateTime now = LocalDateTime.now();
        BeanUtils.copyProperties(dto, task, "id", "createdAt", "updatedAt", "version");
        task.setUpdatedAt(now);

        int rows = taskMapper.updateById(task);
        if (rows == 0) {
            return ServiceResp.fail(ResponseCode.BAD_REQUEST.getCode(), "数据已被修改，请刷新后重试");
        }

        TaskLog taskLog = new TaskLog();
        taskLog.setTaskId(task.getId());
        taskLog.setEmployeeId(dto.getOperator());
        taskLog.setAction(task.getStatus());
        taskLog.setCreatedAt(now);
        taskLogMapper.insert(taskLog);

        return ServiceResp.success(task);
    }

    public List<TaskDTO> getProjectCachedTasks(Long projectId) {
        return taskCache.get(buildProjectKey(projectId));
    }

    public void cacheProjectTasks(Long projectId, List<TaskDTO> dtos) {
        taskCache.put(buildProjectKey(projectId), dtos);
    }

    public void evictProjectTasks(Long projectId) {
        taskCache.remove(buildProjectKey(projectId));
    }

    private String buildProjectKey(Long projectId) {
        return "project:" + projectId;
    }
}
