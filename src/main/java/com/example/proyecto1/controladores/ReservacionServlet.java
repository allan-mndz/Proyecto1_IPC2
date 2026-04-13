package com.example.proyecto1.controladores;

import com.example.proyecto1.dao.ReservacionDAO;
import com.example.proyecto1.modelos.Reservacion;
import com.google.gson.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "ReservacionServlet", urlPatterns = {"/api/reservaciones"})
public class ReservacionServlet extends HttpServlet {

    private ReservacionDAO reservacionDAO = new ReservacionDAO();

    private Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {

        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            String date = json.getAsString();
            if (date == null || date.trim().isEmpty()) {
                return null;
            }
            try {
                return LocalDate.parse(date);
            } catch (Exception e) {
                return null;
            }
        }
    }).create();

    @Override
    protected void doGet (HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String numeroReservacion = request.getParameter("numeroReservacion");
            if (numeroReservacion == null || numeroReservacion.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Debe especificar el número de reservación\"}");
                return;
            }

            List<Reservacion> reservacion = reservacionDAO.obtenerReservaciones(numeroReservacion);
            String jsonRespuesta = gson.toJson(reservacion);
            response.setStatus(HttpServletResponse.SC_OK);
            out.print(jsonRespuesta);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error interno al procesar la solicitud\"}");
        } finally {
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            Reservacion nuevaRes = gson.fromJson(request.getReader(), Reservacion.class);

            // Generamos un código único basado en el reloj del sistema (Ej. RES-168502830)
            nuevaRes.setNumeroReservacion("RES-" + System.currentTimeMillis());

            nuevaRes.setFechaCreacion(LocalDate.now());

            // Estado inicial: Pendiente
            nuevaRes.setEstado("Pendiente");

            if (reservacionDAO.insertarReservacion(nuevaRes)) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                out.print("{\"mensaje\": \"Reservación creada con éxito\", \"numeroRes\": \"" + nuevaRes.getNumeroReservacion() + "\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"Error de base de datos al guardar reservación\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Error al leer los datos de la reservación\"}");
        } finally {
            out.flush();
        }
    }
}
