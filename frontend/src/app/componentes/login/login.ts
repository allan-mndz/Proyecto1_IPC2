import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})

export class Login {
  credenciales={
    nombre: '',
    password: ''
  };

  constructor(private http: HttpClient, private router: Router){}

  iniciarSesion(){
    console.log("Intentando iniciar sesion con:", this.credenciales);

    const urlBackend = 'http://localhost:8080/api/login';

    this.http.post(urlBackend, this.credenciales).subscribe({
      next: (respuesta: any) =>{
        console.log("Exito del servidor:", respuesta);
        localStorage.setItem('rolUsuario', respuesta.rol)
        this.router.navigate(['/dashboard']);
      },
      error:(error) => {
        console.error("Error del servidor:", error);
        alert("Credenciales incorrectas, intente de nuevo.");
      }
    });
  }
}
