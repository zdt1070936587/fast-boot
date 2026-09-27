package com.fastboot.modules.${packageName}.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fastboot.modules.${packageName}.infrastructure.persistence.entity.${aggregate}Entity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ${aggregate}Mapper extends BaseMapper<${aggregate}Entity> {}
