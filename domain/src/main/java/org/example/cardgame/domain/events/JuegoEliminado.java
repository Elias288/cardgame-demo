package org.example.cardgame.domain.events;

import org.example.cardgame.domain.values.JugadorId;

public class JuegoEliminado extends co.com.sofka.domain.generic.DomainEvent {
    public JuegoEliminado(){
        super("cardgame.juegoeliminado");
    }
}
