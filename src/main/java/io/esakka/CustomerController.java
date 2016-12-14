package io.esakka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import io.esakka.domain.model.Protocol;
import io.esakka.dto.CustomerDTO;
import io.esakka.framework.AggregateManager;
import io.esakka.domain.service.MongoEventRepository;
import io.esakka.domain.service.Neo4jSnapshotRepository;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Created by pieter on 12/8/16.
 */
@RestController
public class CustomerController {

    private final ActorRef aggregateManager;

    public CustomerController(final ActorRef aggregateManager) {
        this.aggregateManager = aggregateManager;
    }

    @PostMapping(value = "/customer/{id}")
    public void changeCustomerName(@PathVariable final String id, @RequestParam final String firstName) {
        Protocol.ChangeFirstName command = new Protocol.ChangeFirstName(firstName);

//        actorSystem.actorSelection("customer/" + id).resolveOne(Timeout.durationToTimeout(FiniteDuration.fromNanos(1000000000)))
//                .onComplete(actorRef -> );

        aggregateManager.tell(new AggregateManager.AggregateCommandAndId(command, id), ActorRef.noSender());


//        Timeout timeout = new Timeout(Duration.create(5, "seconds"));
//        Future<Object> future = Patterns.ask(actorRef, command, timeout);
//        Try<Customer> result = Try.of(() -> (Customer) Await.result(future, timeout.duration()));
    }

    @PostMapping(value = "/customer")
    public void changeCustomerName(@RequestBody final CustomerDTO newCustomer) {
        final String aggregateId = UUID.randomUUID().toString();
        final Protocol.CreateCustomer createCustomer = new Protocol.CreateCustomer(
                aggregateId,
                newCustomer.getFirstName(),
                newCustomer.getLastName(),
                newCustomer.getEmail());

        aggregateManager.tell(new AggregateManager.AggregateCommandAndId(createCustomer, aggregateId), ActorRef.noSender());
    }

    @PostMapping(value = "/customer/{id}/validate")
    public void changeCustomerName(@PathVariable final String id) {
        final Protocol.ValidateCustomer validateCustomer = new Protocol.ValidateCustomer();
        aggregateManager.tell(new AggregateManager.AggregateCommandAndId(validateCustomer, id), ActorRef.noSender());
    }
}
