package com.fastboot.modules.${packageName}.domain;

import com.fastboot.ddd.domain.model.AggregateRoot;

/** ${description} */
public class ${aggregate} extends AggregateRoot<Long> {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
