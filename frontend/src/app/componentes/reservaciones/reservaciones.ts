import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-reservaciones',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './reservaciones.html',
  styleUrl: './reservaciones.css',
})

export class Reservaciones implements OnInit{
  nuevaReservacion = {
    clienteDpi: '',
    paqueteNombre: '',
    fechaViaje: '',
    cantidadPersonas: 1,
    costoTotal: 0
  };

  clienteNombre: string = '';
  
  listaPaquetes: any[] = [];
  listaReservaciones: any[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.cargarCatalogos();
    // this.cargarReservaciones();
  }

  cargarCatalogos() {
    this.http.get('http://localhost:8080/api/paquetes').subscribe({
      next: (datos: any) => this.listaPaquetes = datos,
      error: (err) => console.error("Error al cargar paquetes", err)
    });
  }

  //Buscar al Cliente por DPI
  buscarCliente() {
    if (!this.nuevaReservacion.clienteDpi) return;

    this.http.get(`http://localhost:8080/api/clientes?dpi=${this.nuevaReservacion.clienteDpi}`).subscribe({
      next: (cliente: any) => {
        if (cliente && cliente.nombre) {
          this.clienteNombre = cliente.nombre;
        }
      },
      error: () => {
        this.clienteNombre = '';
        alert("Cliente no encontrado. Por favor, regístralo primero en la pestaña de Clientes.");
      }
    });
  }

  //Calculadora se ejecuta si se cambia el paquete o la cantidad de personas
  calcularTotal() {
    if (!this.nuevaReservacion.paqueteNombre || this.nuevaReservacion.cantidadPersonas < 1) {
      this.nuevaReservacion.costoTotal = 0;
      return;
    }

    // Buscamos el precio del paquete seleccionado en nuestra lista
    const paqueteSeleccionado = this.listaPaquetes.find(p => p.nombre === this.nuevaReservacion.paqueteNombre);
    
    if (paqueteSeleccionado) {
      this.nuevaReservacion.costoTotal = paqueteSeleccionado.precio * this.nuevaReservacion.cantidadPersonas;
    }
  }

  guardarReservacion() {
    if (!this.clienteNombre) {
      alert("Debes ingresar un DPI válido antes de reservar.");
      return;
    }

    const datosParaJava = {
      paqueteNombre: this.nuevaReservacion.paqueteNombre,
      fechaViaje: this.nuevaReservacion.fechaViaje,
      cantidadPasajeros: this.nuevaReservacion.cantidadPersonas,
      costoTotal: this.nuevaReservacion.costoTotal,
      dpisPasajeros: [this.nuevaReservacion.clienteDpi], 
      agenteNombre: localStorage.getItem('nombreUsuario')
    };

    const urlBackend = 'http://localhost:8080/api/reservaciones';
    
    this.http.post(urlBackend, datosParaJava).subscribe({
      next: (respuesta: any) => {
        // Mostramos el número dinámico que viene desde Java
        alert("Reservación creada con exito! Tu numero es: " + respuesta.numeroRes);
        
        this.nuevaReservacion = { clienteDpi: '', paqueteNombre: '', fechaViaje: '', cantidadPersonas: 1, costoTotal: 0 };
        this.clienteNombre = '';
      },
      error: () => alert("Error al crear la reservación. Revisa la consola de Tomcat.")
    });
  }
}
