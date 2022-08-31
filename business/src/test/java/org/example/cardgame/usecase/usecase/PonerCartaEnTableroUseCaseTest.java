package org.example.cardgame.usecase.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import org.example.cardgame.domain.command.PonerCartaEnTablero;
import org.example.cardgame.domain.events.*;
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
class PonerCartaEnTableroUseCaseTest {
    @Mock
    private JuegoDomainEventRepository repository;

    @InjectMocks
    private PonerCartaEnTableroUseCase useCase;

    @Test
    void ponerCarta(){
        var command = new PonerCartaEnTablero();
        command.setJuegoId("XXXX");
        command.setJugadorId("GIANNI");
        command.setCartaId("car1");

        when(repository.obtenerEventosPor("XXXX")).thenReturn(crearTodo());

        StepVerifier.create(useCase.apply(Mono.just(command)))
                .expectNextMatches(domainEvent -> {
                    var event = (CartaPuestaEnTablero) domainEvent;
                    assert event.getTableroId().value().equals("TABLERO");
                    return event.getCarta().value().cartaId().value().equals("car1");
                })
                .expectNextMatches(domainEvent -> {
                    var event = (CartaQuitadaDelMazo) domainEvent;
                    assert event.getJugadorId().value().equals("GIANNI");
                    return event.getCarta().value().cartaId().value().equals("car1");
                })
                .expectComplete()
                .verify();
    }

    private Flux<DomainEvent> crearTodo() {
        var event = new JuegoCreado(JugadorId.of("GIANNI"));
        event.setAggregateRootId("XXXX");

        var event1 = new JugadorAgregado(JugadorId.of("GIANNI"), "Gianni", new Mazo(Set.of(
                new Carta(CartaMaestraId.of("car1"), 10, false, true),
                new Carta(CartaMaestraId.of("car2"), 10, false, true),
                new Carta(CartaMaestraId.of("car3"), 10, false, true),
                new Carta(CartaMaestraId.of("car4"), 10, false, true),
                new Carta(CartaMaestraId.of("car5"), 10, false, true),
                new Carta(CartaMaestraId.of("car6"), 10, false, true)
        )));
        event1.setAggregateRootId("XXXX");

        var event1_5 = new JugadorAgregado(JugadorId.of("NIERI"), "Mati", new Mazo(Set.of(
                new Carta(CartaMaestraId.of("cara"), 10, false, true),
                new Carta(CartaMaestraId.of("carb"), 10, false, true),
                new Carta(CartaMaestraId.of("carc"), 10, false, true),
                new Carta(CartaMaestraId.of("card"), 10, false, true),
                new Carta(CartaMaestraId.of("care"), 10, false, true),
                new Carta(CartaMaestraId.of("carf"), 10, false, true)
        )));
        event1_5.setAggregateRootId("XXXX");

        var event2 = new TableroCreado(TableroId.of("TABLERO"), Set.of(JugadorId.of("GIANNI"), JugadorId.of("NIERI")));
        event2.setAggregateRootId("XXXX");

        var event3 = new RondaCreada(new Ronda(1, event2.getJugadorIds()), 30);
        event3.setAggregateRootId("XXXX");

        var event4 = new RondaIniciada();
        event4.setAggregateRootId("XXXX");


        return Flux.just(event, event1, event1_5, event2, event3, event4);
    }
}