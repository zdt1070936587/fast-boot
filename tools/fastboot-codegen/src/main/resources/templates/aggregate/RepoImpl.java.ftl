package com.fastboot.modules.${packageName}.infrastructure.persistence;

import com.fastboot.modules.${packageName}.domain.${aggregate};
import com.fastboot.modules.${packageName}.domain.${aggregate}Repository;
import com.fastboot.modules.${packageName}.infrastructure.mapper.${aggregate}Mapper;
import com.fastboot.modules.${packageName}.infrastructure.persistence.entity.${aggregate}Entity;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ${aggregate}RepositoryImpl implements ${aggregate}Repository {
    private final ${aggregate}Mapper mapper;

    public ${aggregate}RepositoryImpl(${aggregate}Mapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<${aggregate}> findById(Long id) {
        ${aggregate}Entity entity = mapper.selectById(id);
        if (entity == null) {
            return Optional.empty();
        }
        ${aggregate} agg = new ${aggregate}();
        agg.setName(entity.getName());
        return Optional.of(agg);
    }

    @Override
    public void save(${aggregate} aggregate) {
        ${aggregate}Entity entity = new ${aggregate}Entity();
        entity.setName(aggregate.getName());
        mapper.insert(entity);
    }
}
