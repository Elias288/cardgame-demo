package org.example.cardgame.usecase.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import org.example.cardgame.domain.Juego;
import org.example.cardgame.domain.command.EliminarJuegoCommand;
import org.example.cardgame.domain.values.JuegoId;
import org.example.cardgame.usecase.gateway.JuegoDomainEventRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class EliminarJuegoUseCase extends UseCaseForCommand<EliminarJuegoCommand>{
    private final JuegoDomainEventRepository repository;

    public EliminarJuegoUseCase(JuegoDomainEventRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flux<DomainEvent> apply(Mono<EliminarJuegoCommand> eliminarJuegoCommand) {
        return eliminarJuegoCommand.flatMapMany((command) -> repository
                .obtenerEventosPor(command.getJuegoId())
                .collectList()
                .flatMapIterable(events -> {
                    var juego = Juego.from(JuegoId.of(command.getJuegoId()), events);
                    juego.eliminarJuego();
                    return juego.getUncommittedChanges();
                }));
    }
}
