package org.example.cardgame.usecase.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import org.example.cardgame.domain.command.EliminarJuegoCommand;
import org.example.cardgame.domain.events.JuegoEliminado;
import org.example.cardgame.usecase.gateway.JuegoDomainEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EliminarJuegoUseCaseTest {

    @Mock
    private JuegoDomainEventRepository repository;

    @InjectMocks
    private EliminarJuegoUseCase useCase;

    @Test
    void EliminarJuego(){
        //ARRANGE
        var command = new EliminarJuegoCommand();
        command.setJuegoId("FFFF");

        //ASSERT & ACT
        when(repository.obtenerEventosPor("FFFF"))
                .thenReturn(juegoEliminado());

        StepVerifier
                .create(useCase.apply(Mono.just(command)))
                .expectNextMatches(domainEvent -> {
                    var event = (JuegoEliminado) domainEvent;
                    return event.aggregateRootId().equals("FFFF");
                })
                .expectComplete()
                .verify();

    }
    private Flux<DomainEvent> juegoEliminado(){
        var event = new JuegoEliminado();
        event.setAggregateRootId("FFFF");

        return Flux.just(event);
    }

}