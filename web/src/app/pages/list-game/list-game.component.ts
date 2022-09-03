import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { JuegoModel } from 'src/app/shared/model/juego';
import { ApiService } from 'src/app/shared/services/api.service';
import { AuthService } from 'src/app/shared/services/auth.service';
import { WebsocketService } from 'src/app/shared/services/websocket.service';


@Component({
  selector: 'app-list-game',
  templateUrl: './list-game.component.html',
  styleUrls: ['./list-game.component.scss']
})
export class ListGameComponent implements OnInit, OnDestroy {
  displayedColumns: string[] = ['alias', 'cantidad', 'iniciado', 'id'];
  dataSource: JuegoModel[] = [];
  
  constructor(
    public api: ApiService,
    public authService: AuthService,
    public ws: WebsocketService,
    public router: Router) {

  }
  ngOnDestroy(): void {
    this.ws.close();
  }

  ngOnInit(): void {
    this.api.getMisJuegos(this.authService.user.uid).subscribe((elements) => {
      this.dataSource = elements;
    });
  }

  entrar(id: string) {
    if(id)
      this.router.navigate(['board', id]);
  }

  iniciar(id: string) {
    this.ws.connect(id).subscribe({
      next: (event:any) => {
        if(event.type === 'cardgame.tablerocreado'){         
          this.api.crearRonda({
              juegoId: id,
              tiempo: 80,
              jugadores: event.jugadorIds.map((it:any) => it.uuid) 
          }).subscribe();
        }

        if(event.type == 'cardgame.rondacreada'){
          this.router.navigate(['board', id]);
        }
      },
      error: (err:any) => console.log(err),
      complete: () => console.log('complete')
    });
    this.api.iniciar({ juegoId: id }).subscribe();
  }

  eliminar(id: string){
    this.ws.connect(id).subscribe({
      next: (event: any) => {
        console.log(event)
        if(event.type === 'cardgame.juegoeliminado'){
          console.log(event.aggregateRootId)
          this.dataSource = this.dataSource.filter((game) => game.id !== event.aggregateRootId)
        }
      }
    })
    this.api.eliminarJuego({ juegoId: id }).subscribe()
  }
}