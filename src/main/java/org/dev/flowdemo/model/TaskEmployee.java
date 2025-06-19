package org.dev.flowdemo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("task_employee")
public class TaskEmployee {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long taskId;
    private Long employeeId;
    private Integer role;
    private LocalDateTime createdAt;
    private Integer status;
}
