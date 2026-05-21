/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.progetto_finale;

import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;

/**
 *
 * @author marco
 */
public class CatalogoServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private static final long serialVersionUID = 1L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		// Ottiene il context della servlet
		ServletContext context = config.getServletContext();
		// Ottiene il percorso in cui è stato spostato il file
		File f = new File(context.getRealPath("northwind.db"));
		// Aggiorna il percorso del database prima che il file venga aperto
		HibernateUtil.SetFilePath(f.getPath());
	}

	/**
	 * Default constructor.
	 */
	public CatalogoServlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		switch (request.getServletPath()) {
		case "/GetCatalogo":
			GetCatalogo(request, response);
			break;
		case "/DeleteCatalogo":
			DeleteCatalogo(request, response);
			break;
		case "/GetAllCatalogo":
			GetAllCatalogo(request, response);
			break;
		default:
			response.getWriter().append("Servizio non esistente");
			response.setStatus(404);
		}

	}

	protected void DeleteCatalogo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CatalogoDao dao = new CatalogoDao();

		if (request.getParameter("CategoryID") != null) {
			int catalogo_id = Integer.parseInt(request.getParameter("CategoryID"));

			if (dao.deleteCatalogo(catalogo_id)) {
				response.getWriter().append("Catalogo eliminato");
			} else {
				response.getWriter().append("Catalogo non trovato");
				response.setStatus(404);
			}

		} else {
			response.getWriter().append("Nessun id del Catalogo richiesto");
			response.setStatus(400);
		}
	}

	protected void GetCatalogo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CatalogoDao dao = new CatalogoDao();

		if (request.getParameter("CategoryID") != null) {
			int catalogo_id = Integer.parseInt(request.getParameter("CategoryID"));
			Catalogo catalogo = dao.getCatalogo(catalogo_id);

			if (catalogo != null) {
				Gson gson = new Gson();
				response.getWriter().append(gson.toJson(catalogo));
				response.setContentType("application/json");
			} else {
				response.getWriter().append("Catalogo non trovato");
				response.setStatus(404);
			}

		} else {
			response.getWriter().append("Nessun id del Catalogo richiesto");
			response.setStatus(400);
		}
	}

	protected void GetAllCatalogo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CatalogoDao dao = new CatalogoDao();

		var catalogo = dao.getAllCatalogo();

		if (catalogo != null) {
			Gson gson = new Gson();
			response.getWriter().append(gson.toJson(catalogo));
			response.setContentType("application/json");
		} else {
			response.getWriter().append("Nessun Catalogo non trovato");
			response.setStatus(404);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
