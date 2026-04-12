import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-proveedores',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './proveedores.html',
  styleUrls: ['./proveedores.css']
})
export class Proveedores implements OnInit {
  
  nuevoProveedor = { nombre: '', tipo: 1, pais: '', contacto: '' };
  listaProveedores: any[] = [];
  editando: boolean = false;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.cargarProveedores();
  }

  cargarProveedores() {
    this.http.get('http://localhost:8080/api/proveedores').subscribe({
      next: (datos: any) => this.listaProveedores = datos,
      error: (err) => console.error("Error al cargar proveedores", err)
    });
  }

  guardarProveedor() {
    const urlBackend = 'http://localhost:8080/api/proveedores';
    
    if (this.editando) {
      this.http.put(urlBackend, this.nuevoProveedor).subscribe({
        next: () => {
          alert("¡Proveedor actualizado!");
          this.limpiarFormulario();
          this.cargarProveedores();
        },
        error: () => alert("Error al actualizar.")
      });
    } else {
      this.http.post(urlBackend, this.nuevoProveedor).subscribe({
        next: () => {
          alert("¡Proveedor guardado!");
          this.limpiarFormulario();
          this.cargarProveedores();
        },
        error: () => alert("Error al guardar.")
      });
    }
  }

  eliminarProveedor(nombre: string) {
    if (confirm(`Eliminar al proveedor ${nombre}?`)) {
      this.http.delete(`http://localhost:8080/api/proveedores?nombre=${nombre}`).subscribe({
        next: () => {
          alert("¡Proveedor eliminado!");
          this.cargarProveedores();
        },
        error: () => alert("Error al eliminar. Podría estar asignado a un paquete.")
      });
    }
  }

  cargarDatosParaEditar(proveedor: any) {
    this.nuevoProveedor = {
      nombre: proveedor.nombre,
      tipo: proveedor.tipo,
      pais: proveedor.pais,
      contacto: proveedor.contacto
    };
    this.editando = true;
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  limpiarFormulario() {
    this.nuevoProveedor = { nombre: '', tipo: 1, pais: '', contacto: '' };
    this.editando = false;
  }
}