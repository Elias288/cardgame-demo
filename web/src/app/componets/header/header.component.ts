import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/shared/services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {


  constructor(private router: Router, private authService: AuthService) { }
  
  isLogged: boolean = this.authService.isLoggedIn
  
  ngOnInit(): void {
  }

  inicio(){
    this.router.navigate(['home'])
  }
  list(){
    this.router.navigate(['list'])
  }
  newGame(){
    this.router.navigate(['new'])
  }
  logout(){
    this.authService.signOut()
    .then(() => {
      this.isLogged = this.authService.isLoggedIn
      this.router.navigate(['login'])
    })
    .catch(er => console.log(er))
  }
}
