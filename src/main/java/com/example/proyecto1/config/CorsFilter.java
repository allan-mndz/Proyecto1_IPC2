package com.example.proyecto1.config;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// El asterisco sirve para proteger todas las urls (login, paquetes, etc.)
@WebFilter("/*")
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // Le decimos a Java que acepte peticiones exclusivamente de tu Angular
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");

        // Permitimos que envíen credenciales
        response.setHeader("Access-Control-Allow-Credentials", "true");

        // Métodos permitidos (GET para consultar, POST para guardar)
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");

        // Tipos de datos permitidos (JSON)
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization");

        // Si es OPTIONS, le decimos que todo esta bien.
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            return;
        }

        // Si no es OPTIONS, dejamos que la petición pase normal a los Servlets
        chain.doFilter(request, response);
    }
}