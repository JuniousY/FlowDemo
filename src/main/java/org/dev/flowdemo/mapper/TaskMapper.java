package org.dev.flowdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dev.flowdemo.dto.req.TaskQueryReq;
import org.dev.flowdemo.model.Task;

import java.util.List;

@Mapper
public interface TaskMapper extends BaseMapper<Task> {

    List<Task> queryTasks(@Param("req") TaskQueryReq req);

}
