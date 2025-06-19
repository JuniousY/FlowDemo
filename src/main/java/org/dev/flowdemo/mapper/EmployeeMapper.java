package org.dev.flowdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.dev.flowdemo.model.Employee;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
