package io.esakka.framework;

import javaslang.collection.List;
import javaslang.control.Try;

import java.util.function.Function;

/**
 * Created by pieter on 12/9/16.
 */
public class BehaviorBuilder {

    private final Function<DomainCommand, Try<List<DomainEvent>>> commandHandler;
    private final Function<DomainEvent, Aggregate> eventHandler;

    public BehaviorBuilder() {
        this.commandHandler = null;
        this.eventHandler = null;
    }

    public BehaviorBuilder(Function<DomainCommand, Try<List<DomainEvent>>> commandHandler, Function<DomainEvent, Aggregate> eventHandler) {
        this.commandHandler = commandHandler;
        this.eventHandler = eventHandler;
    }


    public BehaviorBuilder setCommandHandler(Function<DomainCommand, Try<List<DomainEvent>>> handler) {
        return new BehaviorBuilder(handler, this.eventHandler);
    }

    public BehaviorBuilder setEventHandler(Function<DomainEvent, Aggregate> handler) {
        return new BehaviorBuilder(this.commandHandler, handler);
    }

    public Behavior build() {
        return new Behavior(commandHandler, eventHandler);
    }

}
