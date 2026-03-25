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

@WebServlet (name = "ReservacionServlet", urlPatterns = {"/reservacion"})
public class ReservacionServlet extends HttpServlet {
    private ReservacionDAO reservacionDAO = new ReservacionDAO();
    private Gson gson = new GsonBuilder().registerTypeAdapter(java.time.LocalDate.class, new com.google.gson.TypeAdapter<java.time.LocalDate>() {

        @Override
        public void write(com.google.gson.stream.JsonWriter out, java.time.LocalDate value) throws IOException {
            out.value(value != null ? value.toString() : null);
        }
        @Override
        public java.time.LocalDate read(com.google.gson.stream.JsonReader in) throws IOException {
            return java.time.LocalDate.parse(in.nextString());
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
}
