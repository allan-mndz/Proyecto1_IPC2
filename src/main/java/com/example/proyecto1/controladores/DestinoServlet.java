package com.example.proyecto1.controladores;

import com.example.proyecto1.dao.DestinoDAO;
import com.example.proyecto1.modelos.Destino;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "DestinoServlet", urlPatterns = {"/api/destinos"})
public class DestinoServlet extends HttpServlet {

    private DestinoDAO destinoDAO = new DestinoDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try{
            List<Destino> destinos = destinoDAO.obtenerTodosLosDestinos();
            String jsonRespuesta = gson.toJson(destinos);
            response.setStatus(HttpServletResponse.SC_OK);
            out.print(jsonRespuesta);
        }catch (Exception e){
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Error interno al cargar los destinos\"}");
        }finally {
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            Destino nuevoDestino = gson.fromJson(request.getReader(), Destino.class);
            boolean guardado = destinoDAO.insertarDestino(nuevoDestino);

            if (guardado) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                out.print("{\"mensaje\": \"Destino guardado con éxito\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"No se pudo guardar en la base de datos\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Error al leer los datos enviados\"}");
        } finally {
            out.flush();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // El manual dice que el nombre es el identificador único del destino
            String nombreDestino = request.getParameter("nombre");
            if (nombreDestino != null && !nombreDestino.isEmpty()) {
                boolean eliminado = destinoDAO.eliminarDestino(nombreDestino);

                if (eliminado) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    out.print("{\"mensaje\": \"Destino eliminado con éxito\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print("{\"error\": \"No se pudo eliminar el destino.\"}");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Falta el nombre del destino a eliminar\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error interno al intentar eliminar\"}");
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
            Destino destinoModificado = gson.fromJson(request.getReader(), Destino.class);

            boolean actualizado = destinoDAO.actualizarDestino(destinoModificado);

            if (actualizado) {
                response.setStatus(HttpServletResponse.SC_OK);
                out.print("{\"mensaje\": \"Destino actualizado con éxito\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"No se pudo actualizar en la base de datos\"}");
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
