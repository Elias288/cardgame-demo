package org.example.cardgame.usecase.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import org.example.cardgame.domain.command.CrearRondaCommand;
import org.example.cardgame.domain.events.JuegoCreado;
import org.example.cardgame.domain.events.RondaCreada;
import org.example.cardgame.domain.events.TableroCreado;
import org.example.cardgame.domain.values.JugadorId;

import org.example.cardgame.domain.values.TableroId;
import org.example.cardgame.usecase.gateway.JuegoDomainEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


//TODO: hacer prueba
@ExtendWith(MockitoExtension.class)
class CrearRondaUseCaseTest {

    @InjectMocks
    private CrearRondaUseCase useCase;

    @Mock
    private JuegoDomainEventRepository repository;

    @Test
    void crearRonda(){
        //ARRANGE
        var command = new CrearRondaCommand();
        command.setJuegoId("XXXX");
        command.setTiempo(60);
        command.setJugadores(Set.of("FFFF", "GGGG", "HHHH"));

        //ASSERT & ACT
        when(repository.obtenerEventosPor("XXXX"))
                .thenReturn(juegoCreado());

        StepVerifier
                .create(useCase.apply(Mono.just(command)))
                .expectNextMatches(domainEvent -> {
                    var event = (RondaCreada) domainEvent;
                    return event.aggregateRootId().equals("XXXX")
                            && event.getTiempo().equals(60)
                            && event.getRonda().value().jugadores()
                            .equals(Set.of(JugadorId.of("FFFF"), JugadorId.of("GGGG"), JugadorId.of("HHHH")));
                })
                .expectComplete()
                .verify();

    }

    private Flux<DomainEvent> juegoCreado() {
        var event = new JuegoCreado(JugadorId.of("FFFF"));
        event.setAggregateRootId("XXXX");

        var event2 = new TableroCreado(TableroId.of("LLLL"), Set.of(JugadorId.of("FFFF"), JugadorId.of("GGGG"), JugadorId.of("HHHH")));
        event2.setAggregateRootId("XXXX");

        return Flux.just(event, event2);
    }
}