package org.dev.flowdemo.dto.req;

import lombok.Data;

@Data
public class TaskQueryReq {

    private Long projectId;

    private String name; // task name or staff name

    private String status;

}
