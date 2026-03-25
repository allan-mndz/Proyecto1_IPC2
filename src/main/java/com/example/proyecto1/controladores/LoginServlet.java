package com.example.proyecto1.controladores;


import com.example.proyecto1.dao.UsuarioDAO;
import com.example.proyecto1.modelos.Usuario;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "LoginServlet", urlPatterns = {"/api/login"})

public class LoginServlet extends HttpServlet {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private Gson gson = new Gson();

    @Override
    protected void doPost(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Gson convierte el JSON que envía Angular directamente a un objeto Usuario de Java
            Usuario credenciales = gson.fromJson(request.getReader(), Usuario.class);

            // Buscamos el usuario en la base de datos usando el DAO
            Usuario usuarioDB = usuarioDAO.obtenerUsuario(credenciales.getNombre());

            // Validamos que el usuario exista y que la contraseña coincida

            if (usuarioDB != null && usuarioDB.getPassword().equals(credenciales.getPassword())) {

                // Creamos la sesión HTTP para recordar quién está logueado
                HttpSession session = request.getSession(true);
                session.setAttribute("usuario", usuarioDB.getNombre());
                session.setAttribute("rol", usuarioDB.getTipo());

                // le decimos a Angular qué rol tiene
                response.setStatus(HttpServletResponse.SC_OK);
                out.print("{\"mensaje\": \"Login exitoso\", \"rol\": " + usuarioDB.getTipo() + "}");

            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print("{\"error\": \"Usuario o contraseña incorrectos\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Error al procesar la solicitud de login\"}");
        } finally {
            out.flush();
        }
    }

}
