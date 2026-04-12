import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-carga-datos',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './carga-datos.html',
  styleUrls: ['./carga-datos.css']
})

export class CargaDatos {
  
  archivoSeleccionado: File | null = null;
  mensajePantalla: string = '';

  constructor(private http: HttpClient) {}

  seleccionarArchivo(event: any) {
    const file: File = event.target.files[0];
    if (file) {
      this.archivoSeleccionado = file;
      this.mensajePantalla = '';
    }
  }

  subirArchivo() {
    if (!this.archivoSeleccionado) {
      this.mensajePantalla = 'Por favor, selecciona un archivo primero.';
      return;
    }

    const formData = new FormData();
    
    formData.append('archivoEntrada', this.archivoSeleccionado);

    this.mensajePantalla = 'Archivo Cargado';

    this.http.post('http://localhost:8080/api/cargar-datos', formData, { responseType: 'text' }).subscribe({
      next: (res: any) => {
        this.mensajePantalla = 'Tu archivo fue procesado y la base de datos está llena.';
        this.archivoSeleccionado = null;
        (document.getElementById('inputArchivo') as HTMLInputElement).value = ''; 
      },
      error: (err) => {
        console.error(err);
        this.mensajePantalla = 'Error al procesar. Revisa la consola de IntelliJ (Tomcat) para ver qué línea falló.';
      }
    });
  }
}