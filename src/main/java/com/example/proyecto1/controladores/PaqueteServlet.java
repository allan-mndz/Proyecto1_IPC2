package com.example.proyecto1.controladores;

import com.example.proyecto1.dao.PaqueteDAO;
import com.example.proyecto1.modelos.Paquete;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


@WebServlet(name = "PaqueteServlet", urlPatterns = {"/api/paquetes"})
public class PaqueteServlet extends HttpServlet {

    private PaqueteDAO paqueteDAO = new PaqueteDAO();
    private Gson gson = new Gson();

    // Usamos doGet porque Angular nos está pidiendo datos (Get)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Configuramos la carta de respuesta para que sea un JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Le pedimos la lista al DAO
            List<Paquete> paquetes = paqueteDAO.obtenerTodosLosPaquetes();

            // Convierte toda la lista de Java a un texto JSON perfecto
            String jsonRespuesta = gson.toJson(paquetes);

            // Enviamos el estado OK y el texto JSON por la red
            response.setStatus(HttpServletResponse.SC_OK);
            out.print(jsonRespuesta);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error interno al cargar los paquetes\"}");
        } finally {
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Gson  leerá el JSON y llenará el Paquete, y llenará automáticamente la List<ServicioPaquete> que está adentro
            com.example.proyecto1.modelos.Paquete nuevoPaquete = gson.fromJson(request.getReader(), com.example.proyecto1.modelos.Paquete.class);

            // Llamamos a nuestra nueva función transaccional
            boolean guardado = paqueteDAO.insertarPaqueteConServicios(nuevoPaquete);

            if (guardado) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                out.print("{\"mensaje\": \"Paquete Turístico y sus servicios guardados con éxito\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"No se pudo guardar el paquete en la base de datos\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Error al leer los datos enviados desde Angular\"}");
        } finally {
            out.flush();
        }
    }
}