package com.example.proyecto1.controladores;

import com.example.proyecto1.dao.PaqueteDAO;
import com.example.proyecto1.modelos.Paquete;
import com.google.gson.Gson;

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
            // 1. Le pedimos la lista al DAO
            List<Paquete> paquetes = paqueteDAO.obtenerTodosLosPaquetes();

            // 2. Convierte toda la lista de Java a un texto JSON perfecto
            String jsonRespuesta = gson.toJson(paquetes);

            // 3. Enviamos el estado OK y el texto JSON por la red
            response.setStatus(HttpServletResponse.SC_OK);
            out.print(jsonRespuesta);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error interno al cargar los paquetes\"}");
        } finally {
            out.flush();
        }
    }
}