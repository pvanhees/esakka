package io.esakka.framework;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import akka.persistence.Eventsourced;
import javaslang.MatchError;
import javaslang.collection.List;
import javaslang.control.Try;

import java.util.concurrent.atomic.AtomicLong;

import static javaslang.API.$;
import static javaslang.API.Case;
import static javaslang.API.Match;
import static javaslang.Predicates.instanceOf;

/**
 * Created by pieter on 12/9/16.
 */
public class EventSourced extends AbstractLoggingActor {
    private final EventRepository eventRepository;
    private final SnapshotRepository snapshotRepository;
    private final String aggregateId;
    private final AggregateSnapshot initialAggregate;

    private AtomicLong eventSequenceNr = new AtomicLong(0L);
    private Aggregate aggregate;

    public EventSourced(final String aggregateId, final Aggregate initialAggregate, final EventRepository eventRepository, final SnapshotRepository snapshotRepository) {
        this.aggregateId = aggregateId;
        this.initialAggregate = new AggregateSnapshot(0L, initialAggregate);
        this.eventRepository = eventRepository;
        this.snapshotRepository = snapshotRepository;

        receive(ReceiveBuilder
                .match(DomainCommand.class, this::handleCommand)
                .matchAny(o -> log().info("Unknown message type " + o.toString()))
                .build()
        );
    }

    private Void handleCommand(final DomainCommand domainCommand) {
        Try<Aggregate> aggregate = this.aggregate
                .behavior
                .handleCommand(domainCommand)
                .map(domainEvents -> {
                    persistEvents(domainEvents);
                    this.aggregate = handleEvents(this.aggregate, domainEvents);
                    return this.aggregate;
                })
                .onFailure(throwable -> Match(throwable).of(
                        Case($(instanceOf(MatchError.class)), Try.failure(new InvalidCommandException())),
                        Case($(), Try.failure(throwable))
                ));
        sender().tell(aggregate, self());
        return null;
    }

    private Aggregate handleEvents(final Aggregate aggregate, final List<DomainEvent> events) {
        return events.foldLeft(
                aggregate,
                (agg, event) -> agg.getBehavior().handleEvent(event));
    }

    protected Void persistEvents(List<DomainEvent> events) {
        events.forEach(event -> {
            event.setSequenceNr(eventSequenceNr.incrementAndGet());
            eventRepository.save(event);
        });
        return null;
    }

    @Override
    public void preStart() throws Exception {
        final AggregateSnapshot aggregateSnapshot = snapshotRepository
                .findLatestSnapshotByAggregateId(aggregateId)
                .orElse(initialAggregate);
        final List<DomainEvent> domainEvents = List.ofAll(eventRepository.findByAggregateIdOrderBySequenceNr(aggregateId));
        this.eventSequenceNr = new AtomicLong(aggregateSnapshot.getSequenceNr());
        this.aggregate = handleEvents(aggregateSnapshot.getAggregate(), domainEvents);
    }

    public static Props props(final String id, final Aggregate initialAggregate, final EventRepository eventRepository, final SnapshotRepository snapshotRepository) {
        return Props.create(EventSourced.class, () -> new EventSourced(id, initialAggregate, eventRepository, snapshotRepository));
    }
}
