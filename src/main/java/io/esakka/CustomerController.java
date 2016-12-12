package io.esakka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import io.esakka.domain.service.CustomerActor;
import io.esakka.framework.AggregateManager;
import io.esakka.domain.service.MongoEventRepository;
import io.esakka.domain.service.Neo4jSnapshotRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by pieter on 12/8/16.
 */
@RestController
public class CustomerController {

    private final ActorSystem actorSystem;
    private final ActorRef aggregateManager;

    public CustomerController(final ActorSystem actorSystem, final MongoEventRepository eventRepository, final Neo4jSnapshotRepository snapshotRepository) {
        this.actorSystem = actorSystem;
        this.aggregateManager = actorSystem.actorOf(AggregateManager.props(eventRepository, snapshotRepository));
    }

    @PostMapping(value = "/customer/{id}")
    public void changeCustomerName(@PathVariable final String id, @RequestParam final String firstName) {
        CustomerActor.ChangeFirstNameCommand command = new CustomerActor.ChangeFirstNameCommand(firstName);

//        actorSystem.actorSelection("customer/" + id).resolveOne(Timeout.durationToTimeout(FiniteDuration.fromNanos(1000000000)))
//                .onComplete(actorRef -> );

        aggregateManager.tell(new AggregateManager.AggregateCommandAndId(command, id), ActorRef.noSender());


//        Timeout timeout = new Timeout(Duration.create(5, "seconds"));
//        Future<Object> future = Patterns.ask(actorRef, command, timeout);
//        Try<Customer> result = Try.of(() -> (Customer) Await.result(future, timeout.duration()));
    }

}
