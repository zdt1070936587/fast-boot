package com.fastboot.modules.${packageName}.application;

import com.fastboot.modules.${packageName}.domain.${aggregate};
import com.fastboot.modules.${packageName}.domain.${aggregate}Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ${aggregate}AppService {
    private final ${aggregate}Repository repository;

    public ${aggregate}AppService(${aggregate}Repository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public ${aggregate} get(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Transactional
    public void save(${aggregate} aggregate) {
        repository.save(aggregate);
    }
}
