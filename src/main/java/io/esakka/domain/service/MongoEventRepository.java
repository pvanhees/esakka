package io.esakka.domain.service;

import io.esakka.framework.DomainEvent;
import io.esakka.framework.EventRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by pieter on 12/8/16.
 */
@Repository
public interface MongoEventRepository extends MongoRepository<DomainEvent, String>, EventRepository {
}
