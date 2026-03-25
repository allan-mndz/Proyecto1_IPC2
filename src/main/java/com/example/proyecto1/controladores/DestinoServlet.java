package com.example.proyecto1.controladores;

import com.example.proyecto1.dao.DestinoDAO;
import com.example.proyecto1.modelos.Destino;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "DestinoServlet", urlPatterns = {"/api/destinos"})
public class DestinoServlet extends HttpServlet {

    private DestinoDAO destinoDAO = new DestinoDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try{
            List<Destino> destinos = destinoDAO.obtenerTodosLosDestinos();
            String jsonRespuesta = gson.toJson(destinos);
            response.setStatus(HttpServletResponse.SC_OK);
            out.print(jsonRespuesta);
        }catch (Exception e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error interno al cargar los destinos\"}");
        }finally {
            out.flush();
        }
    }
}
