package com.example.proyecto1.controladores;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "PagoServlet", urlPatterns = {"/api/pagos"})
public class PagoServlet extends HttpServlet {

    private com.example.proyecto1.dao.PagoDAO pagoDAO = new com.example.proyecto1.dao.PagoDAO();

    private com.google.gson.Gson gson = new com.google.gson.GsonBuilder()
            .registerTypeAdapter(java.time.LocalDate.class, new com.google.gson.JsonSerializer<java.time.LocalDate>() {
                @Override
                public com.google.gson.JsonElement serialize(java.time.LocalDate src, java.lang.reflect.Type typeOfSrc, com.google.gson.JsonSerializationContext context) {
                    return new com.google.gson.JsonPrimitive(src.toString()); // Enseña a enviar fechas a Angular
                }
            })
            .registerTypeAdapter(java.time.LocalDate.class, new com.google.gson.JsonDeserializer<java.time.LocalDate>() {
                @Override
                public java.time.LocalDate deserialize(com.google.gson.JsonElement json, java.lang.reflect.Type typeOfT, com.google.gson.JsonDeserializationContext context) {
                    String date = json.getAsString();
                    if (date == null || date.trim().isEmpty()) return null;
                    try { return java.time.LocalDate.parse(date); } catch (Exception e) { return null; } // Enseña a recibir fechas de Angular
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

            List<com.example.proyecto1.modelos.Pago> pagos = pagoDAO.obtenerPagosPorReservacion(numRes);
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
            com.example.proyecto1.modelos.Pago nuevoPago = gson.fromJson(request.getReader(), com.example.proyecto1.modelos.Pago.class);

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