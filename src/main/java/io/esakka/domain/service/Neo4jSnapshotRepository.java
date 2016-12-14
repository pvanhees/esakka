package io.esakka.domain.service;

import io.esakka.domain.model.Customer;
import io.esakka.framework.SnapshotRepository;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by pieter on 12/8/16.
 */
@Repository
public interface Neo4jSnapshotRepository extends GraphRepository<Customer>, SnapshotRepository {

}
