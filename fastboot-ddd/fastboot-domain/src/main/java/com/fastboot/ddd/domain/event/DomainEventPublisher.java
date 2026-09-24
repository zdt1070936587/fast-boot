package com.fastboot.ddd.domain.event;

import com.fastboot.ddd.domain.model.AggregateRoot;

/**
 * 领域事件发布端口。
 *
 * @author zhaodt
 * @since 2026-09-24
 */
public interface DomainEventPublisher {
    void publish(Object event);

    default void publishAll(AggregateRoot<?> aggregate) {
        for (Object event : aggregate.pullEvents()) {
            publish(event);
        }
    }
}
