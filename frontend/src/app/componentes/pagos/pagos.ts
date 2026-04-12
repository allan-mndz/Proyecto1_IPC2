import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-pagos',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './pagos.html',
  styleUrl: './pagos.css',
})

export class Pagos {
  nuevoPago = {
    numeroReservacion: '',
    monto: null,
    metodo: 1, // 1: Efectivo, 2: Tarjeta, 3: Transferencia
    fecha: this.obtenerFechaHoy()
  };

  listaPagos: any[] = [];

  constructor(private http: HttpClient) {}

  obtenerFechaHoy(): string {
    const hoy = new Date();
    return hoy.toISOString().split('T')[0];
  }

  buscarPagos() {
    if (!this.nuevoPago.numeroReservacion) return;

    this.http.get(`http://localhost:8080/api/pagos?numeroReservacion=${this.nuevoPago.numeroReservacion}`).subscribe({
      next: (datos: any) => this.listaPagos = datos,
      error: (err) => console.error("Error al buscar pagos", err)
    });
  }

  registrarPago() {
    if (!this.nuevoPago.numeroReservacion || !this.nuevoPago.monto) {
      alert("Por favor llena el número de reservación y el monto.");
      return;
    }

    this.http.post('http://localhost:8080/api/pagos', this.nuevoPago).subscribe({
      next: () => {
        alert("Pago con exito!");
        this.buscarPagos();
        this.nuevoPago.monto = null; 
      },
      error: () => alert("Error al registrar el pago. Revisa que el código de reservación exista.")
    });
  }
}
