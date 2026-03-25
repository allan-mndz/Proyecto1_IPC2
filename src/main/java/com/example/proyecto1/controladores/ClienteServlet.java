package com.example.proyecto1.controladores;

import com.example.proyecto1.dao.ClienteDAO;
import com.example.proyecto1.modelos.Cliente;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ClienteServlet extends HttpServlet {

    private ClienteDAO clienteDAO = new ClienteDAO();
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
            List<Cliente> clientes = clienteDAO.obtenerTodosLosClientes();
            String jsonRespuesta = gson.toJson(clientes);
            response.setStatus(jakarta.servlet.http.HttpServletResponse.SC_OK);
            out.print(jsonRespuesta);
        }catch (Exception e) {
            response.setStatus(jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error interno al cargar los clientes\"}");
        } finally {
            out.flush();
        }

    }

}
