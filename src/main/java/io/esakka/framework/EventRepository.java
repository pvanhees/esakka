package io.esakka.framework;

import java.util.List;

/**
 * Created by pieter on 12/9/16.
 */
public interface EventRepository {

    Void save(DomainEvent event);

    List<DomainEvent> findByAggregateIdOrderBySequenceNr(String aggregateId);

}
