import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterOutlet, RouterLink],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})

export class Dashboard implements OnInit{
  rol: number = 0;

  constructor(private router: Router){}

  ngOnInit(){
    const rolGuardado = localStorage.getItem('rolUsuario');

    if(rolGuardado){
      this.rol = parseInt(rolGuardado, 10);
    }else{
      this.router.navigate(['/login']);
    }
  }

  cerrarSesion(){
    localStorage.removeItem('rolUsuario');
    this.router.navigate(['/login']);
  }
}
