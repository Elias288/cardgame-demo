package org.example.cardgame.domain.command;

import co.com.sofka.domain.generic.Command;

public class EliminarJuegoCommand extends Command {
    private String id;

    /**
     * Gets juego id
     *
     * @return the juego id
     */
    public String getJuegoId() {
        return this.id;
    }

    /**
    * Sets juego id
    *
    * @param juegoId the juego id
    */
    public void setJuegoId(String juegoId) {
        this.id = juegoId;
    }
}
