package com.example.proyecto1.controladores;

import com.example.proyecto1.dao.PagoDAO;
import com.example.proyecto1.modelos.Pago;
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

@WebServlet(name = "PagoServlet", urlPatterns = {"/api/pagos"})
public class PagoServlet extends HttpServlet {

    private PagoDAO pagoDAO = new PagoDAO();

    private Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {

        @Override
        public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString()); // Enseña a enviar fechas a Angular
        }
    }).registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
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
            } // Enseña a recibir fechas de Angular
        }
    }).create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String numRes = request.getParameter("numeroReservacion");
            if (numRes == null || numRes.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Debe especificar el número de reservación\"}");
                return;
            }

            List<Pago> pagos = pagoDAO.obtenerPagosPorReservacion(numRes);
            response.setStatus(HttpServletResponse.SC_OK);
            out.print(gson.toJson(pagos));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error interno al procesar el pago\"}");
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
            Pago nuevoPago = gson.fromJson(request.getReader(), Pago.class);

            int resultado = pagoDAO.procesarPago(nuevoPago);

            if (resultado == 2) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                out.print("{\"mensaje\": \"Pago registrado con éxito. ¡Reservación Confirmada!\", \"confirmada\": true}");
            } else if (resultado == 1) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                out.print("{\"mensaje\": \"Pago registrado. La reservación sigue Pendiente.\", \"confirmada\": false}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"Error al guardar en la base de datos. Verifica la reservación.\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Error al leer los datos enviados\"}");
        } finally {
            out.flush();
        }
    }
}