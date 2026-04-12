import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-clientes',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './clientes.html',
  styleUrls: ['./clientes.css']
})
export class Clientes implements OnInit {
  
  nuevoCliente = { dpi: '', nombre: '', fechaNacimiento: '', telefono: '', email: '', nacionalidad: '' };
  
  listaClientes: any[] = [];
  editando: boolean = false;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.cargarClientes();
  }

  cargarClientes() {
    this.http.get('http://localhost:8080/api/clientes').subscribe({
      next: (datos: any) => this.listaClientes = datos,
      error: (err) => console.error("Error al cargar clientes", err)
    });
  }

  buscarPorDPI() {
    if (!this.nuevoCliente.dpi) return;

    this.http.get(`http://localhost:8080/api/clientes?dpi=${this.nuevoCliente.dpi}`).subscribe({
      next: (clienteEncontrado: any) => {
        if (clienteEncontrado && clienteEncontrado.nombres) {
          this.nuevoCliente = clienteEncontrado;
          this.editando = true;
          alert("Cliente encontrado. Puedes actualizar sus datos.");
        }
      },
      error: (err) => {
        console.log("El cliente no existe, procedemos a crearlo como nuevo.");
        this.editando = false; 
      }
    });
  }

  guardarCliente() {
    const urlBackend = 'http://localhost:8080/api/clientes';
    
    if (this.editando) {
      this.http.put(urlBackend, this.nuevoCliente).subscribe({
        next: () => {
          alert("¡Cliente actualizado!");
          this.limpiarFormulario();
          this.cargarClientes();
        },
        error: () => alert("Error al actualizar cliente.")
      });
    } else {
      this.http.post(urlBackend, this.nuevoCliente).subscribe({
        next: () => {
          alert("¡Cliente registrado con éxito!");
          this.limpiarFormulario();
          this.cargarClientes();
        },
        error: () => alert("Error al registrar cliente.")
      });
    }
  }

  limpiarFormulario() {
    this.nuevoCliente = { dpi: '', nombre: '', fechaNacimiento: '', telefono: '', email: '', nacionalidad: '' };
    this.editando = false;
  }
}
