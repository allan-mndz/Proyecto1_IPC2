import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-destinos',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './destinos.html',
  styleUrl: './destinos.css',
})

export class Destinos implements OnInit{
  nuevoDestino = { nombre: '', pais: '', descripcion: '', clima: '', imagen: '' };
  listaDestinos: any[] = [];
  editando: boolean = false;

  constructor(private http: HttpClient){}

  ngOnInit() {
    this.cargarDestinos();
  }

  cargarDestinos() {
    this.http.get('http://localhost:8080/api/destinos').subscribe({
      next: (datos: any) => {
        this.listaDestinos = datos;
      },
      error: (err) => console.error("Error al cargar la tabla", err)
    });
  }

  cargarDatosParaEditar(destinoSeleccionado: any) {
  this.nuevoDestino = {
    nombre: destinoSeleccionado.nombre,
    pais: destinoSeleccionado.pais,
    descripcion: destinoSeleccionado.descripcion,
    clima: destinoSeleccionado.clima,
    imagen: destinoSeleccionado.imagen || destinoSeleccionado.imagen_url
  };
  this.editando = true;
  window.scrollTo({ top: 0, behavior: 'smooth' });
}

  guardarDestino() {
  const urlBackend = 'http://localhost:8080/api/destinos';

  if (this.editando) {
    // MODO EDICIÓN (PUT)
    this.http.put(urlBackend, this.nuevoDestino).subscribe({
      next: (respuesta: any) => {
        alert("¡Destino actualizado correctamente!");
        this.limpiarFormulario();
        this.cargarDestinos();
      },
      error: (err) => {
        console.error(err);
        alert("Error al actualizar el destino.");
      }
    });
  } else {
    // MODO CREACIÓN (POST)
    this.http.post(urlBackend, this.nuevoDestino).subscribe({
      next: (respuesta: any) => {
        alert("¡Destino guardado exitosamente!");
        this.limpiarFormulario();
        this.cargarDestinos();
      },
      error: (err) => {
        console.error(err);
        alert("Error al guardar el destino.");
      }
    });
  }
}
  limpiarFormulario() {
    this.nuevoDestino = { nombre: '', pais: '', descripcion: '', clima: '', imagen: '' };
    this.editando = false;
  }

  eliminarDestino(nombre: string) {
    const confirmacion = confirm(`Seguro de que deseas eliminar el destino: ${nombre}?`);
    
    if (confirmacion) {
      const urlDelete = `http://localhost:8080/api/destinos?nombre=${nombre}`;
      
      this.http.delete(urlDelete).subscribe({
        next: () => {
          alert("¡Destino eliminado del sistema!");
          this.cargarDestinos(); 
        },
        error: (err) => {
          console.error("Error al eliminar:", err);
          alert("No se pudo eliminar el destino. Es posible que esté siendo usado en algún Paquete Turístico.");
        }
      });
    }
  }
}
