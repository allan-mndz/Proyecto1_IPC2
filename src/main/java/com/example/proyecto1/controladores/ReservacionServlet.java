package com.example.proyecto1.controladores;

import com.example.proyecto1.dao.ReservacionDAO;
import com.example.proyecto1.modelos.Reservacion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ReservacionServlet", urlPatterns = {"/api/reservaciones"})
public class ReservacionServlet extends HttpServlet {

    private ReservacionDAO reservacionDAO = new ReservacionDAO();
    private com.google.gson.Gson gson = new com.google.gson.GsonBuilder()
            .registerTypeAdapter(java.time.LocalDate.class, new com.google.gson.JsonDeserializer<java.time.LocalDate>() {
                @Override
                public java.time.LocalDate deserialize(com.google.gson.JsonElement json, java.lang.reflect.Type typeOfT, com.google.gson.JsonDeserializationContext context) {
                    String date = json.getAsString();
                    if (date == null || date.trim().isEmpty()) return null;
                    try { return java.time.LocalDate.parse(date); } catch (Exception e) { return null; }
                }
            }).create();

    @Override
    protected void doGet (jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws java.io.IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        java.io.PrintWriter out = response.getWriter();

        try {
            String numeroReservacion = request.getParameter("numeroReservacion");
            if (numeroReservacion == null || numeroReservacion.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Debe especificar el número de reservación\"}");
                return;
            }

            List<Reservacion> reservacion = reservacionDAO.obtenerReservaciones(numeroReservacion);
            String jsonRespuesta = gson.toJson(reservacion);
            response.setStatus(jakarta.servlet.http.HttpServletResponse.SC_OK);
            out.print(jsonRespuesta);

        } catch (Exception e) {
            response.setStatus(jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error interno al procesar la solicitud\"}");
        } finally {
            out.flush();
        }
    }

    @Override
    protected void doPost(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws java.io.IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        java.io.PrintWriter out = response.getWriter();

        try {
            com.example.proyecto1.modelos.Reservacion nuevaRes = gson.fromJson(request.getReader(), com.example.proyecto1.modelos.Reservacion.class);

            // Generamos un código único basado en el reloj del sistema (Ej. RES-168502830)
            nuevaRes.setNumeroReservacion("RES-" + System.currentTimeMillis());

            nuevaRes.setFechaCreacion(java.time.LocalDate.now());

            // Estado inicial: Pendiente
            nuevaRes.setEstado("Pendiente");

            if (reservacionDAO.insertarReservacion(nuevaRes)) {
                response.setStatus(jakarta.servlet.http.HttpServletResponse.SC_CREATED);
                out.print("{\"mensaje\": \"Reservación creada con éxito\", \"numeroRes\": \"" + nuevaRes.getNumeroReservacion() + "\"}");
            } else {
                response.setStatus(jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"Error de base de datos al guardar reservación\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Error al leer los datos de la reservación\"}");
        } finally {
            out.flush();
        }
    }
}
