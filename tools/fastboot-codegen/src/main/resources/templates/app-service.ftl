package com.fastboot.modules.${packageName}.application;

import com.fastboot.modules.${packageName}.domain.${aggregateName};
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ${aggregateName} 应用服务骨架。
 */
@Service
public class ${aggregateName}AppService {

    @Transactional
    public ${aggregateName} create(String name) {
        ${aggregateName} ${aggregateVar} = new ${aggregateName}();
        ${aggregateVar}.rename(name);
        return ${aggregateVar};
    }
}
