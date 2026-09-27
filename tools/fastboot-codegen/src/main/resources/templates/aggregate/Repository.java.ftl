package com.fastboot.modules.${packageName}.domain;

import java.util.Optional;

public interface ${aggregate}Repository {
    Optional<${aggregate}> findById(Long id);

    void save(${aggregate} aggregate);
}
