package org.dev.flowdemo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskDTO {

    // 请求id，用uuid生成
    private String reqId;

    private Long projectId;

    private Long taskId;

    private String name;

    private String description;

    private Integer progress; // 进度百分比(0-100)

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    private Integer timeSpent; // 完成耗时(分钟)

    private Long assigneeId;

    private String assigneeName;

    private Integer status;

    private String statusDesc;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    private Integer version;

    private Long operator;
}
