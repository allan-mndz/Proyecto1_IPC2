package com.example.proyecto1.controladores;

import com.example.proyecto1.dao.ProveedorDAO;
import com.example.proyecto1.modelos.Proveedor;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            Proveedor nuevoProveedor = gson.fromJson(request.getReader(), Proveedor.class);
            if (proveedorDAO.insertarProveedor(nuevoProveedor)) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                out.print("{\"mensaje\": \"Proveedor guardado con éxito\"}");
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
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            Proveedor provModificado = gson.fromJson(request.getReader(), Proveedor.class);
            if (proveedorDAO.actualizarProveedor(provModificado)) {
                response.setStatus(HttpServletResponse.SC_OK);
                out.print("{\"mensaje\": \"Proveedor actualizado con éxito\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"No se pudo actualizar en la BD\"}");
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
            String nombre = request.getParameter("nombre");
            if (nombre != null && !nombre.isEmpty()) {
                if (proveedorDAO.eliminarProveedor(nombre)) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    out.print("{\"mensaje\": \"Proveedor eliminado con éxito\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print("{\"error\": \"No se pudo eliminar el proveedor\"}");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Falta el nombre del proveedor\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error interno al eliminar\"}");
        } finally {
            out.flush();
        }
    }
}
