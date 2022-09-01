package org.example.cardgame.usecase.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import org.example.cardgame.domain.events.JuegoCreado;
import org.example.cardgame.domain.events.JuegoFinalizado;
import org.example.cardgame.domain.events.JugadorAgregado;
import org.example.cardgame.domain.events.RondaTerminada;
import org.example.cardgame.domain.values.*;
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

@ExtendWith(MockitoExtension.class)
class DeterminarGanadorUseCaseTest {
    @InjectMocks
    private DeterminarGanadorUseCase useCase;

    @Mock
    private JuegoDomainEventRepository repository;

    @Test
    void determinarGanador(){
        //ARRANGE
        var event = new RondaTerminada(TableroId.of("DDD"), Set.of(JugadorId.of("FFFF"), JugadorId.of("XXXX")));
        event.setAggregateRootId("GGGGG");

        //ACT & ASSERT
        when(repository.obtenerEventosPor("GGGGG"))
                .thenReturn(historial());

        StepVerifier.create(useCase.apply(Mono.just(event)))
                .expectNextMatches(domainEvent ->{
                    var event2 = (JuegoFinalizado) domainEvent;
                    return event2.getJugadorId().equals(JugadorId.of("FFFF")) && event2.getAlias().equals("Raul");
                })
                .expectComplete()
                .verify();
    }

    private Flux<DomainEvent> historial() {
        var event = new JuegoCreado(JugadorId.of("FFFF"));
        event.setAggregateRootId("GGGGG");

        var event2 = new JugadorAgregado(
                JugadorId.of("FFFF"), "Raul",
                new Mazo(Set.of(
                        new Carta(CartaMaestraId.of("sss"), 1000, true, true),
                        new Carta(CartaMaestraId.of("bbb"), 102, true, true),
                        new Carta(CartaMaestraId.of("ccc"), 101, true, true),
                        new Carta(CartaMaestraId.of("ddd"), 104, true, true),
                        new Carta(CartaMaestraId.of("fff"), 150, true, true),
                        new Carta(CartaMaestraId.of("ggg"), 160, true, true)
                )));
        event2.setAggregateRootId("GGGGG");

        return Flux.just(event, event2);
    }
}