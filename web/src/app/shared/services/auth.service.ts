import { Injectable } from '@angular/core';
import firebase from 'firebase/compat/app';
import { AngularFireAuth } from '@angular/fire/compat/auth';
import {
  AngularFirestore,
  AngularFirestoreDocument,
} from '@angular/fire/compat/firestore';
import { Router } from '@angular/router';
import { User } from '../model/user';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  userData: any;
  puntaje: number = 0

  constructor(
    public afs: AngularFirestore,
    public afAuth: AngularFireAuth,
    public router: Router,
  ) {
    this.afAuth.authState.subscribe((user) => {
      if (user) {
        this.userData = user;
        localStorage.setItem('user', JSON.stringify(this.userData));
        JSON.parse(localStorage.getItem('user')!);

        this.afs.collection<any>(`users`).snapshotChanges().subscribe((actions) => {
          actions.map((item: any) => {
            const data = item.payload.doc.data();
            const selected = this.userData.uid === data.uid;
            if(selected){
              localStorage.setItem('puntaje', JSON.stringify(data.puntos !== undefined ? data.puntos : 0))
              JSON.parse(localStorage.getItem('puntaje')!);
              this.puntaje = data.puntos !== undefined ? data.puntos : 0
            }
          });
        })

      } else {
        localStorage.setItem('user', 'null');
        localStorage.setItem('puntaje', 'null');
        JSON.parse(localStorage.getItem('user')!);
        JSON.parse(localStorage.getItem('puntaje')!);
      }
    });

  }

  get user() {
    if (this.isLoggedIn) {
      return JSON.parse(localStorage.getItem('user')!);
    }
    throw new Error("No found uid");
  }

  get isLoggedIn(): boolean {
    const user = JSON.parse(localStorage.getItem('user')!);
    return user !== null && user.emailVerified !== false ? true : false;
  }

  googleAuth() {
    return this.authLogin(new firebase.auth.GoogleAuthProvider())
      .then((res: any) => {
        if (res) window.location.href = '/home';
      })
      .catch((err) => {
        if(err) window.location.href = '/login';
      });
  }

  authLogin(provider: any) {
    return this.afAuth
      .signInWithPopup(provider)
      .then(async(result: any) => {
        await this.setUserData(result.user)
        return result.user
      })
      .catch((error: any) => {
        window.alert(error);
        if(error) window.location.href = '/login';
      });
  }

  setUserData(user: any) {
    const userRef: AngularFirestoreDocument<any> = this.afs.doc(
      `users/${user.uid}`
    );

    const userData: User = {
      uid: user.uid,
      email: user.email,
      displayName: user.displayName,
      photoURL: user.photoURL,
      emailVerified: user.emailVerified
    };
    
    return userRef.set(userData, {
      merge: true,
    });

  }

  setUserPuntos(userId: string, puntos: number){
    const userRef: AngularFirestoreDocument<any> = this.afs.doc(
      `users/${userId}`
    );
    console.log(puntos)
    console.log(this.puntaje)
    userRef.update({ puntos: this.puntaje + puntos })
  }

  signOut() {
    return this.afAuth.signOut().then(() => {
      localStorage.removeItem('user');
      this.router.navigate(['login']);
    });
  }
}