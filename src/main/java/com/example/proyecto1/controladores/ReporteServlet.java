package com.example.proyecto1.controladores;

import com.example.proyecto1.dao.ReporteDAO;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ReporteServlet", urlPatterns = {"/api/reportes"})
public class ReporteServlet extends HttpServlet {

    private ReporteDAO reporteDAO = new ReporteDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String tipoReporte = request.getParameter("tipo");
        String fechaInicio = request.getParameter("inicio");
        String fechaFin = request.getParameter("fin");

        if (tipoReporte == null || fechaInicio == null || fechaFin == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Faltan parámetros de fecha o tipo de reporte\"}");
            return;
        }

        try {
            List<Map<String, Object>> resultado = null;

            switch (tipoReporte) {
                case "1":
                    resultado = reporteDAO.reporteVentas(fechaInicio, fechaFin); break;
                case "2":
                    resultado = reporteDAO.reporteCancelaciones(fechaInicio, fechaFin); break;
                case "3":
                    resultado = reporteDAO.reporteGanancias(fechaInicio, fechaFin); break;
                case "4":
                    resultado = reporteDAO.reporteAgenteMasVentas(fechaInicio, fechaFin); break;
                case "5":
                    resultado = reporteDAO.reporteAgenteMasGanancias(fechaInicio, fechaFin); break;
                case "6":
                    resultado = reporteDAO.reportePaqueteMasVendido(fechaInicio, fechaFin); break;
                case "7":
                    resultado = reporteDAO.reportePaqueteMenosVendido(fechaInicio, fechaFin); break;
                case "8":
                    resultado = reporteDAO.reporteOcupacionDestino(fechaInicio, fechaFin); break;
                default:
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\": \"Tipo de reporte no válido\"}");
                    return;
            }

            response.setStatus(HttpServletResponse.SC_OK);
            out.print(gson.toJson(resultado));

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error al generar el reporte\"}");
        } finally {
            out.flush();
        }
    }
}