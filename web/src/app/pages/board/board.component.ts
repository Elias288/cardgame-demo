import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Carta } from 'src/app/shared/model/tablero';
import { ApiService } from 'src/app/shared/services/api.service';
import { AuthService } from 'src/app/shared/services/auth.service';
import { WebsocketService } from 'src/app/shared/services/websocket.service';

//TODO: componente para el tablero de juego
@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.scss']
})
export class BoardComponent implements OnInit {

  cartasDelJugador: Carta[] = [];
  cartasDelTablero: Carta[] = [];
  tiempo: number = 0;
  jugadoresRonda: number = 0;
  jugadoresTablero: number = 0;
  numeroRonda: number = 0;
  juegoId: string = "";
  uid: string = "";
  roundStarted:boolean = false;

  puntaje: number = 0;
  message: string = "";

  constructor(
    public api: ApiService,
    public authService: AuthService,
    public ws: WebsocketService,
    private route: ActivatedRoute
    ) { }

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.juegoId = params['id'];
      this.uid = this.authService.user.uid;
      this.api.getMiMazo(this.uid, this.juegoId).subscribe((element:any) => {
        this.cartasDelJugador = element.cartas;
      });

      this.api.getTablero(this.juegoId).subscribe((element) => {
        
        this.cartasDelTablero = Object.entries(element.tablero.cartas).flatMap((a: any) => {
          return a[1];
        });
        this.tiempo = element.tiempo;
        this.jugadoresRonda = element.ronda.jugadores.length;
        this.jugadoresTablero = element.tablero.jugadores.length;
        this.numeroRonda = element.ronda.numero;
      });

      this.ws.connect(this.juegoId).subscribe({
        next: (event:any) => {

          if (event.type === 'cardgame.ponercartaentablero') {
            this.cartasDelTablero.push({
              cartaId: event.carta.cartaId.uuid,
              poder: event.carta.poder,
              estaOculta: event.carta.estaOculta,
              estaHabilitada: event.carta.estaHabilitada,
            });
          }
          if (event.type === 'cardgame.cartaquitadadelmazo') {
            this.cartasDelJugador = this.cartasDelJugador
              .filter((item) => item.cartaId !==  event.carta.cartaId.uuid);
          }
          if (event.type === 'cardgame.tiempocambiadodeltablero') {
            this.tiempo = event.tiempo;
          }

          if (event.type === 'cardgame.rondacreada') {
            this.tiempo = event.tiempo;
            this.jugadoresRonda = event.ronda.jugadores.length
            this.numeroRonda = event.ronda.numero
          }

          if(event.type === 'cardgame.rondainiciada'){
            this.roundStarted = true;
          }

          if(event.type === 'cardgame.rondaterminada'){
            this.roundStarted = false;
            this.cartasDelTablero=[]

          }

          if(event.type === 'cardgame.cartasasignadasajugador'){
            if(event.ganadorId.uuid === this.uid){
              event.cartasApuesta.forEach((carta: any) => {
                this.cartasDelJugador.push({
                  cartaId: carta.cartaId.uuid,
                  poder: carta.poder,
                  estaOculta: carta.estaOculta,
                  estaHabilitada: carta.estaHabilitada
                });
              });
              //SETEAR PUNTOS AL USUARIO
              this.puntaje+=event.puntos
              this.authService.setUserPuntos(JSON.parse(localStorage.getItem('user')!).uid, this.puntaje)
              
              this.message = "Ganaste " + event.puntos + " puntaje";
            }else{
              this.message = "perdiste"
            }
          }

        },
        error: (err:any) => console.log(err),
        complete: () => console.log('complete')
      });
    });
  }

  ngOnDestroy(): void {
    this.ws.close();
  }

  poner(cartaId: string, poder: number) {
    if(this.roundStarted){
      this.api.ponerCarta({
        cartaId: cartaId,
        juegoId: this.juegoId,
        jugadorId: this.uid
      }).subscribe();
    }
  }

  iniciarRonda(){
    this.message = ""
    this.api.iniciarRonda({
      juegoId: this.juegoId,
    }).subscribe();
  }

}
