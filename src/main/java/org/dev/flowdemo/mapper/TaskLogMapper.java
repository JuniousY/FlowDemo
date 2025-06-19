package org.dev.flowdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.dev.flowdemo.model.TaskLog;

@Mapper
public interface TaskLogMapper extends BaseMapper<TaskLog> {
}
