package org.example.cardgame.usecase.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import org.example.cardgame.domain.command.IniciarJuegoCommand;
import org.example.cardgame.domain.events.JuegoCreado;

import org.example.cardgame.domain.events.TableroCreado;
import org.example.cardgame.domain.values.JugadorId;
import org.example.cardgame.domain.values.TableroId;
import org.example.cardgame.usecase.gateway.JuegoDomainEventRepository;
import org.example.cardgame.usecase.gateway.ListaDeCartaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IniciarJuegoUseCaseTest {

    @Mock
    private JuegoDomainEventRepository repository;
    @InjectMocks
    private IniciarJuegoUseCase useCase;

    @Test
    void iniciarJuego(){
        //ARRANGE
        var command = new IniciarJuegoCommand();
        command.setJuegoId("FFF");

        //ASSERT & ACT
        when(repository.obtenerEventosPor("FFF"))
                .thenReturn(juegoCreado());

        StepVerifier
                .create(useCase.apply(Mono.just(command)))
                .expectNextMatches(domainEvent -> {
                    var event = (TableroCreado) domainEvent;
                    return event.aggregateRootId().equals("FFF");
                })
                .expectComplete()
                .verify();
    }

    private Flux<DomainEvent> juegoCreado(){
        var event = new JuegoCreado(JugadorId.of("DDDD"));
        event.setAggregateRootId("FFF");

        return Flux.just(event);
    }
}