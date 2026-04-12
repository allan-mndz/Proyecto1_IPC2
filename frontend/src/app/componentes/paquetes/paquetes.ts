import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-paquetes',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './paquetes.html',
  styleUrl: './paquetes.css',
})

export class Paquetes implements OnInit{
  nuevoPaquete = {
    nombre: '',
    destinoNombre: '', 
    duracion: null,
    precio: null,
    capacidad: null,
    descripcion: '',
    servicios: [] as any[] 
};

  servicioTemporal = {
    proveedorNombre: '', 
    descripcion: '',
    costo: null
  };

  listaDestinos: any[] = [];
  listaProveedores: any[] = [];
  listaPaquetes: any[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.cargarCatalogos();
    this.cargarPaquetes();
  }

  cargarCatalogos() {
    this.http.get('http://localhost:8080/api/destinos').subscribe({
      next: (datos: any) => this.listaDestinos = datos
    });
    this.http.get('http://localhost:8080/api/proveedores').subscribe({
      next: (datos: any) => this.listaProveedores = datos
    });
  }

  cargarPaquetes() {
  this.http.get('http://localhost:8080/api/paquetes').subscribe({
    next: (datos: any) => {
      this.listaPaquetes = datos;
    },
    error: (err) => console.error("Error al cargar los paquetes", err)
  });
}

  agregarServicio() {
    if (this.servicioTemporal.proveedorNombre && this.servicioTemporal.descripcion && this.servicioTemporal.costo) {
      this.nuevoPaquete.servicios.push({ ...this.servicioTemporal });
      
      // Limpiamos los inputs del servicio temporal
      this.servicioTemporal = { proveedorNombre: '', descripcion: '', costo: null };
    } else {
      alert("Llena todos los datos del servicio (Proveedor, Descripción y Costo).");
    }
  }

  eliminarServicio(index: number) {
    this.nuevoPaquete.servicios.splice(index, 1);
  }

  guardarPaquete() {
    if (this.nuevoPaquete.servicios.length === 0) {
      alert("El paquete debe tener al menos un servicio incluido.");
      return;
    }

    console.log("Enviando paquete completo a Java:", this.nuevoPaquete);
    
    this.http.post('http://localhost:8080/api/paquetes', this.nuevoPaquete).subscribe({
      next: (res) => {
        alert("Paquete Turístico creado!");
        // Limpiamos todo
        this.nuevoPaquete = { nombre: '', destinoNombre: '', duracion: null, precio: null, capacidad: null, descripcion: '', servicios: [] };
        this.cargarPaquetes();
      },
      error: (err) => alert("Error al guardar el paquete.")
    });
    
  }
}
