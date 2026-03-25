package com.example.proyecto1.controladores;


import com.example.proyecto1.utilidades.LectorDatos;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

@WebServlet(name = "CargaDatosServlet", urlPatterns = {"/api/cargar-datos"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
        maxFileSize = 1024 * 1024 * 10,       // 10MB
        maxRequestSize = 1024 * 1024 * 50     // 50MB
)


public class CargaDatosServlet extends HttpServlet {
    private LectorDatos lector = new LectorDatos();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Configuramos la respuesta para que Angular entienda que es un JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            Part filePart = request.getPart("archivoEntrada");

            if (filePart != null && filePart.getSize() > 0) {
                InputStream fileContent = filePart.getInputStream();
                lector.procesarArchivo(fileContent);
                response.setStatus(jakarta.servlet.http.HttpServletResponse.SC_OK);
                out.write("{\"mensaje\": \"Archivo procesado exitosamente.\"}");

            } else {
                response.setStatus(jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"No se recibió ningún archivo\"}");
            }
        } catch (Exception e) {
            response.setStatus(jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error interno del servidor: " + e.getMessage() + "\"}");
        }finally {
            out.flush();
        }
    }
}
