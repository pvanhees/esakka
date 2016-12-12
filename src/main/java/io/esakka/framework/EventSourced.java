package io.esakka.framework;

import akka.actor.AbstractLoggingActor;
import akka.japi.pf.ReceiveBuilder;
import javaslang.collection.List;
import javaslang.control.Try;

/**
 * Created by pieter on 12/9/16.
 */
public abstract class EventSourced<A extends Aggregate> extends AbstractLoggingActor {

    private final EventRepository eventRepository;
    private final SnapshotRepository<A> snapshotRepository;
    private final String aggregateId;

    private long eventSequenceNr = 0;
    private Aggregate aggregate;

    public EventSourced(final String aggregateId, final EventRepository eventRepository, final SnapshotRepository<A> snapshotRepository) {
        this.eventRepository = eventRepository;
        this.snapshotRepository = snapshotRepository;
        this.aggregateId = aggregateId;

        receive(ReceiveBuilder
                .match(DomainCommand.class, this::handleCommand)
                .matchAny(o -> log().info("Unknown message " + o.toString()))
                .build()
        );
    }

    private Void handleCommand(final DomainCommand domainCommand) {
        Try<Aggregate> aggregate = this.aggregate
                .getBehavior()
                .handleCommand(domainCommand)
                .map(result -> {
                    List<DomainEvent> domainEvents = result._2;
                    domainEvents.forEach(event -> event.setSequenceNr(eventSequenceNr++));
                    persistEvents(domainEvents);
                    this.aggregate = result._1;
                    return this.aggregate;
                });
        sender().tell(aggregate, self());
        return null;
    }

    protected Void persistEvents(List<DomainEvent> events) {
        events.forEach(eventRepository::save);
        return null;
    }

    @Override
    public void preStart() throws Exception {
        final A aggregateSnapshot = snapshotRepository.findByAggregateId(aggregateId);
        List.ofAll(eventRepository
                .findByAggregateIdOrderBySequenceNr(aggregateId))
                .foldLeft(aggregateSnapshot, this::handleEvent);
    }

    protected abstract A handleEvent(final A aggregate, final DomainEvent domainEvent);
}
