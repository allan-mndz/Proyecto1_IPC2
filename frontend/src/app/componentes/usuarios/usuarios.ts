import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-usuarios',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './usuarios.html',
  styleUrl: './usuarios.css',
})

export class Usuarios implements OnInit{
  nuevoUsuario = {
    nombre: '',
    password: '',
    tipo: 1 // 1: Atención al Cliente, 2: Operaciones, 3: Administrador
  };

  listaUsuarios: any[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.cargarUsuarios();
  }

  cargarUsuarios() {
    this.http.get('http://localhost:8080/api/usuarios').subscribe({
      next: (datos: any) => this.listaUsuarios = datos,
      error: (err) => console.error("Error al cargar usuarios", err)
    });
  }

  guardarUsuario() {
    if (!this.nuevoUsuario.nombre || !this.nuevoUsuario.password) {
      alert("Por favor llena el nombre de usuario y la contraseña.");
      return;
    }

    this.http.post('http://localhost:8080/api/usuarios', this.nuevoUsuario).subscribe({
      next: () => {
        alert("¡Usuario creado exitosamente!");
        this.nuevoUsuario = { nombre: '', password: '', tipo: 1 };
        this.cargarUsuarios();
      },
      error: () => alert("Error al crear usuario. Verifica que el nombre no esté repetido.")
    });
  }
}
