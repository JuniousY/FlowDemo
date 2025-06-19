package org.dev.flowdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.dev.flowdemo.model.Project;

@Mapper
public interface ProjectMapper extends BaseMapper<Project> {
}
