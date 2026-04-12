package com.example.proyecto1.controladores;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "UsuarioServlet", urlPatterns = {"/api/usuarios"})
public class UsuarioServlet extends HttpServlet {

    private com.example.proyecto1.dao.UsuarioDAO usuarioDAO = new com.example.proyecto1.dao.UsuarioDAO();
    private com.google.gson.Gson gson = new com.google.gson.Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            List<com.example.proyecto1.modelos.Usuario> usuarios = usuarioDAO.obtenerTodosLosUsuarios();
            response.setStatus(HttpServletResponse.SC_OK);
            out.print(gson.toJson(usuarios));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error al cargar usuarios\"}");
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
            com.example.proyecto1.modelos.Usuario nuevoUsuario = gson.fromJson(request.getReader(), com.example.proyecto1.modelos.Usuario.class);

            if (usuarioDAO.insertarUsuario(nuevoUsuario)) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                out.print("{\"mensaje\": \"Usuario guardado con exito\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"No se pudo guardar el usuario.\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Error al leer los datos enviados\"}");
        } finally {
            out.flush();
        }
    }
}