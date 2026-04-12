import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

@Component({
  selector: 'app-reportes',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './reportes.html'
})


export class Reportes {
  configReporte = {
    tipo: '1',
    inicio: this.obtenerFechaHaceUnMes(),
    fin: this.obtenerFechaHoy()
  };

  nombreAuthor = 'Allan_mndz';
  fechaNow = new Date().toLocaleDateString();

  datosReporte: any[] = [];
  columnas: string[] = [];

  constructor(private http: HttpClient) {}

  obtenerFechaHoy(): string { return new Date().toISOString().split('T')[0]; }
  obtenerFechaHaceUnMes(): string {
    let d = new Date();
    d.setMonth(d.getMonth() - 1);
    return d.toISOString().split('T')[0];
  }

  // trae los datos y los muestra
  generarVistaPrevia() {
    const url = `http://localhost:8080/api/reportes?tipo=${this.configReporte.tipo}&inicio=${this.configReporte.inicio}&fin=${this.configReporte.fin}`;
    
    this.http.get(url).subscribe({
      next: (datos: any) => {
        this.datosReporte = datos;
        if (this.datosReporte.length > 0) {
          this.columnas = Object.keys(this.datosReporte[0]);
        } else {
          alert("No se encontraron registros para este rango.");
        }
      },
      error: () => alert("Error al conectar con el servidor.")
    });
  }

  exportarPDF() {
    const pdf = new jsPDF('p', 'mm', 'letter');
    
    pdf.setFont('Helvetica', 'bold');
    pdf.setFontSize(20);
    pdf.text('Reporte de Operaciones', 14, 20);

    pdf.setFontSize(10);
    pdf.setFont('Helvetica', 'normal');
    pdf.text(`Generado por: ${this.nombreAuthor}`, 14, 28);
    pdf.text(`Fecha de emisión: ${this.fechaNow}`, 14, 33);
    pdf.text(`Período: ${this.configReporte.inicio} al ${this.configReporte.fin}`, 14, 38);

    // Limpiamos los títulos
    const columnasLimpias = ['#', ...this.columnas.map(col => col.replace(/_/g, ' ').toUpperCase())];

    const filasTabla = this.datosReporte.map((fila, index) => {
      const valoresFila = this.columnas.map(col => fila[col]);
      return [index + 1, ...valoresFila];
    });

    autoTable(pdf, {
      head: [columnasLimpias],
      body: filasTabla,
      startY: 45,
      theme: 'grid',
      headStyles: { fillColor: [52, 58, 64] }
    });

    pdf.save(`Reporte_Tipo_${this.configReporte.tipo}.pdf`);
  }
}