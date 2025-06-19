package org.dev.flowdemo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("employee")
public class Employee {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long organizationId;
    private String name;
    private String email;
    private String phone;
    private String position;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
