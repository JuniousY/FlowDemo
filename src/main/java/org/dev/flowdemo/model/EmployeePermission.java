package org.dev.flowdemo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("employee_permission")
public class EmployeePermission {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long employeeId;
    private Long permissionId;
    private LocalDateTime createdAt;
}
