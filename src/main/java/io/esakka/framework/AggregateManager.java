package io.esakka.framework;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import io.esakka.domain.service.CustomerActor;
import io.esakka.domain.service.MongoEventRepository;
import io.esakka.domain.service.Neo4jSnapshotRepository;
import javaslang.Tuple;
import javaslang.collection.HashMap;
import javaslang.collection.Map;


public class AggregateManager extends AbstractLoggingActor{

    public static class AggregateCommandAndId {
        private final DomainCommand command;
        private final String aggregateId;

        public AggregateCommandAndId(final DomainCommand command, final String aggregateId) {
            this.command = command;
            this.aggregateId = aggregateId;
        }
    }

    private Map<String, ActorRef> aggregateActors = HashMap.empty();
    private final MongoEventRepository eventRepository;
    private final Neo4jSnapshotRepository snapshotRepository;

    public AggregateManager(final MongoEventRepository eventRepository, final Neo4jSnapshotRepository snapshotRepository) {
        this.eventRepository = eventRepository;
        this.snapshotRepository = snapshotRepository;
        receive(ReceiveBuilder
                .match(AggregateCommandAndId.class, this::commandReceived)
                .matchAny(this::unhandled)
                .build()
        );
    }

    private void commandReceived(final AggregateCommandAndId commandAndId) {
        final ActorRef aggregateActor = aggregateActors
                .get(commandAndId.aggregateId)
                .getOrElse(() -> createAggregateActor(commandAndId)); //lazy eval
        aggregateActor.tell(commandAndId.command, self());
    }

    private ActorRef createAggregateActor(final AggregateCommandAndId commandAndId) {
        log().info("Actor not found, creating new actor");
        final String aggregateId = commandAndId.aggregateId;
        final ActorRef actorRef = context().actorOf(CustomerActor.props(aggregateId, eventRepository, snapshotRepository));
        this.aggregateActors = aggregateActors.put(Tuple.of(aggregateId, actorRef));
        return actorRef;
    }

    public static Props props(final MongoEventRepository eventRepository, final Neo4jSnapshotRepository snapshotRepository) {
        return Props.create(AggregateManager.class, () -> new AggregateManager(eventRepository, snapshotRepository));
    }
}
