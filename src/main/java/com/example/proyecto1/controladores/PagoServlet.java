package com.example.proyecto1.controladores;

import com.example.proyecto1.dao.PagoDAO;
import com.example.proyecto1.modelos.Pago;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class PagoServlet extends HttpServlet {

    private PagoDAO pagoDao = new PagoDAO();
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

    protected void doGet(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws IOException {
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

            List<Pago> pagos = pagoDao.obtenerPagosPorReservacion(numRes);
            String jsonRespuesta = gson.toJson(pagos);
            response.setStatus(jakarta.servlet.http.HttpServletResponse.SC_OK);
            out.print(jsonRespuesta);
        } catch (Exception e) {
            response.setStatus(jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error interno al procesar el pago\"}");
        }finally {
            out.flush();
        }
    }
}
