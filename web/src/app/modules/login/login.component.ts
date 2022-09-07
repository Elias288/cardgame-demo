import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/shared/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  spin: boolean = true;

  constructor(public auth: AuthService) { }
  ngOnInit() {
  }
  
  login() {
    this.spin=false
    this.auth.googleAuth()
  }

}
