package io.esakka.configuration;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import io.esakka.domain.model.Customer;
import io.esakka.domain.service.MongoEventRepository;
import io.esakka.domain.service.Neo4jSnapshotRepository;
import io.esakka.framework.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by pieter on 12/8/16.
 */
@Configuration
public class AkkaConfig {

    @Autowired
    private MongoEventRepository eventRepository;

    @Autowired
    private Neo4jSnapshotRepository snapshotRepository;

    @Bean
    ActorSystem actorSystem() {
        return ActorSystem.create("CustomerAggregateSystem");
    }

    @Bean
    ActorRef aggregateManager() {
        actorSystem().actorOf(
                AggregateManager.props(new Aggregate() {
                    @Override
                    public String getAggregateId() {
                        return null;
                    }

                    @Override
                    public Behavior getBehavior() {
                        return Customer.initialBehavior;
                    }
                }, eventRepository, snapshotRepository);
    }
}
