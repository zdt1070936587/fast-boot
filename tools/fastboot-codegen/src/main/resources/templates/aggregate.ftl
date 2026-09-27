package com.fastboot.modules.${packageName}.domain;

import com.fastboot.ddd.domain.model.AggregateRoot;

/**
 * ${aggregateName} 聚合根（生成骨架，请补充业务不变量）。
 */
public class ${aggregateName} extends AggregateRoot<Long> {

    private String name;

    public String getName() {
        return name;
    }

    public void rename(String name) {
        this.name = name;
    }
}
