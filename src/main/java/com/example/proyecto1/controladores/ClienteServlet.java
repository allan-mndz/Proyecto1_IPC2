package com.example.proyecto1.controladores;

import com.example.proyecto1.dao.ClienteDAO;
import com.example.proyecto1.modelos.Cliente;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "ClienteServlet", urlPatterns = {"/api/clientes"})
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Revisamos si Angular nos mandó un DPI específico a buscar
            String dpiBuscado = request.getParameter("dpi");

            if (dpiBuscado != null && !dpiBuscado.isEmpty()) {
                // Buscar solo un cliente
                Cliente cliente = clienteDAO.obtenerClientePorDpi(dpiBuscado);
                if (cliente != null) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    out.print(gson.toJson(cliente));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404: No existe
                    out.print("{\"error\": \"Cliente no encontrado\"}");
                }
            } else {
                // Cargar todos los clientes
                List<Cliente> clientes = clienteDAO.obtenerTodosLosClientes();
                response.setStatus(HttpServletResponse.SC_OK);
                out.print(gson.toJson(clientes));
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error interno en el servidor\"}");
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
            Cliente nuevoCliente = gson.fromJson(request.getReader(), Cliente.class);
            if (clienteDAO.insertarCliente(nuevoCliente)) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                out.print("{\"mensaje\": \"Cliente guardado con éxito\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"No se pudo guardar\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Error al leer datos\"}");
        } finally {
            out.flush();
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            Cliente clienteModificado = gson.fromJson(request.getReader(), Cliente.class);
            if (clienteDAO.actualizarCliente(clienteModificado)) {
                response.setStatus(HttpServletResponse.SC_OK);
                out.print("{\"mensaje\": \"Cliente actualizado\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"No se pudo actualizar\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Error al leer datos\"}");
        } finally {
            out.flush();
        }
    }
}

