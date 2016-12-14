package io.esakka.framework;

import javaslang.collection.List;
import javaslang.control.Try;

import java.util.function.Function;

/**
 * Created by pieter on 12/12/2016.
 */
public class Behavior {

    private final Function<DomainCommand, Try<List<DomainEvent>>> commandHandler;
    private final Function<DomainEvent, Aggregate> eventHandler;


    public Behavior(final Function<DomainCommand, Try<List<DomainEvent>>> commandHandler, final Function<DomainEvent, Aggregate> eventHandler) {
        this.commandHandler = commandHandler;
        this.eventHandler = eventHandler;
    }

    public Aggregate handleEvent(final DomainEvent event) {
        return eventHandler.apply(event);
    }

    public Try<List<DomainEvent>> handleCommand(final DomainCommand command) {
        return commandHandler.apply(command);
    }

}
