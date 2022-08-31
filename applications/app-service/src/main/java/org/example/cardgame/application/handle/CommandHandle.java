package org.example.cardgame.application.handle;

import org.example.cardgame.domain.command.*;
import org.example.cardgame.usecase.usecase.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
@Configuration
public class CommandHandle {
    private IntegrationHandle integrationHandle;
    private ErrorHandler errorHandler;

    public CommandHandle(IntegrationHandle integrationHandle, ErrorHandler errorHandler) {
        this.integrationHandle = integrationHandle;
        this.errorHandler = errorHandler;
    }
    @Bean
    public RouterFunction<ServerResponse> crear(CrearJuegoUseCase usecase) {

        return route(
                POST("/api/juego/crear").and(accept(MediaType.APPLICATION_JSON)),
                request -> usecase.andThen(integrationHandle)
                        .apply(request.bodyToMono(CrearJuegoCommand.class))
                        .then(ServerResponse.ok().build())
                        .onErrorResume(errorHandler::badRequest));
    }

    @Bean
    public RouterFunction<ServerResponse> iniciarRonda(IniciarRondaUseCase useCase){
        return route(
                POST("/api/juego/ronda/iniciar").and(accept(MediaType.APPLICATION_JSON)),
                request -> useCase.andThen(integrationHandle)
                        .apply(request.bodyToMono(IniciarRondaCommand.class))
                        .then(ServerResponse.ok().build())
                        .onErrorResume(errorHandler::badRequest));

    }

    @Bean
    public RouterFunction<ServerResponse> iniciar(IniciarJuegoUseCase useCase){
        return route(
                POST("/api/juego/iniciar").and(accept(MediaType.APPLICATION_JSON)),
                request -> useCase.andThen(integrationHandle)
                        .apply(request.bodyToMono(IniciarJuegoCommand.class))
                        .then(ServerResponse.ok().build())
                        .onErrorResume(errorHandler::badRequest));

    }

    @Bean
    public RouterFunction<ServerResponse> poner(PonerCartaEnTableroUseCase usecase) {
        return route(
                POST("/api/juego/poner").and(accept(MediaType.APPLICATION_JSON)),
                request -> usecase.andThen(integrationHandle)
                        .apply(request.bodyToMono(PonerCartaEnTablero.class))
                        .then(ServerResponse.ok().build())
                        .onErrorResume(errorHandler::badRequest)

        );
    }

    @Bean
    public RouterFunction<ServerResponse> crearRonda(CrearRondaUseCase usecase) {
        return route(
                POST("/api/juego/crear/ronda").and(accept(MediaType.APPLICATION_JSON)),
                request -> usecase.andThen(integrationHandle)
                        .apply(request.bodyToMono(CrearRondaCommand.class))
                        .then(ServerResponse.ok().build())
                        .onErrorResume(errorHandler::badRequest)

        );
    }

}
