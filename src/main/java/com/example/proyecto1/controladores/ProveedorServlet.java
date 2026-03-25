package com.example.proyecto1.controladores;

import com.example.proyecto1.dao.ProveedorDAO;
import com.example.proyecto1.modelos.Proveedor;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "ProveedorServlet", urlPatterns = {"/api/proveedores"})
public class ProveedorServlet extends HttpServlet {

    private ProveedorDAO proveedorDAO = new ProveedorDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try{
            List<Proveedor> proveedores = proveedorDAO.obtenerTodosLosProveedores();
            String jsonRespuesta = gson.toJson(proveedores);
            response.setStatus(HttpServletResponse.SC_OK);
            out.print(jsonRespuesta);
        }catch (Exception e){
            response.setStatus(jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error interno al cargar los proveedores\"}");
        }finally {
            out.flush();
        }
    }

}
