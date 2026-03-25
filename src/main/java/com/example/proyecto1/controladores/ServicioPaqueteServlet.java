package com.example.proyecto1.controladores;

import com.example.proyecto1.dao.ServicioPaqueteDAO;
import com.example.proyecto1.modelos.ServicioPaquete;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ServicioPaqueteServlet", urlPatterns = {"/api/servicio-paquete"})
public class ServicioPaqueteServlet extends HttpServlet {
    private ServicioPaqueteDAO servicioPaqueteDAO = new ServicioPaqueteDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        java.io.PrintWriter out = response.getWriter();

        try {
            String nombrePaquete = request.getParameter("paquete");
            if (nombrePaquete == null || nombrePaquete.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Debe especificar el nombre del paquete\"}");
                return;
            }

            List<ServicioPaquete>  servicioPaquetes = servicioPaqueteDAO.obtenerServiciosPorPaquete(nombrePaquete);
            String jsonRespuesta = gson.toJson(servicioPaquetes);
            response.setStatus(HttpServletResponse.SC_OK);
            out.print(jsonRespuesta);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error interno al cargar los servicios de paquete\"}");
        } finally {
            out.flush();
        }
    }

}
