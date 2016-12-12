package io.esakka.framework;

import javaslang.collection.List;
import javaslang.control.Try;

/**
 * Created by pieter on 12/9/16.
 */
public interface Behavior {

    Try<List<DomainEvent>> handleCommand(DomainCommand command);

    Aggregate handleEvent(DomainEvent event);

}
