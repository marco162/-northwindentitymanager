package com.mycompany.progetto_finale.web;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mycompany.progetto_finale.dao.CategoryDao;
import com.mycompany.progetto_finale.dto.CategoryPayload;
import com.mycompany.progetto_finale.model.Category;
import com.mycompany.progetto_finale.util.HibernateUtil;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

@WebServlet(name = "CategoryServlet", urlPatterns = "/api/categories")
public class CategoryServlet extends HttpServlet {

    private final transient CategoryDao categoryDao = new CategoryDao();
    private final transient Gson gson = new Gson();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        File databaseFile = new File(context.getRealPath("/northwind.db"));
        HibernateUtil.setDatabasePath(databaseFile.getAbsolutePath());
        HibernateUtil.getSessionFactory();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.isBlank()) {
            Integer id = parseId(idParam, response);
            if (id == null) {
                return;
            }

            Category category = categoryDao.findById(id);
            if (category == null) {
                writeJson(response, HttpServletResponse.SC_NOT_FOUND,
                        new ApiMessage("Categoria non trovata."));
                return;
            }

            writeJson(response, HttpServletResponse.SC_OK, CategoryView.fromEntity(category));
            return;
        }

        List<CategoryView> result = categoryDao.findAll().stream()
                .map(CategoryView::fromEntity)
                .toList();
        writeJson(response, HttpServletResponse.SC_OK, result);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CategoryPayload payload = readPayload(request, response);
        if (payload == null) {
            return;
        }

        Category created = categoryDao.create(payload);
        writeJson(response, HttpServletResponse.SC_CREATED, CategoryView.fromEntity(created));
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            writeJson(response, HttpServletResponse.SC_BAD_REQUEST,
                    new ApiMessage("Specifica l'id della categoria da modificare."));
            return;
        }

        Integer id = parseId(idParam, response);
        if (id == null) {
            return;
        }

        CategoryPayload payload = readPayload(request, response);
        if (payload == null) {
            return;
        }

        Category updated = categoryDao.update(id, payload);
        if (updated == null) {
            writeJson(response, HttpServletResponse.SC_NOT_FOUND,
                    new ApiMessage("Categoria non trovata."));
            return;
        }

        writeJson(response, HttpServletResponse.SC_OK, CategoryView.fromEntity(updated));
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            writeJson(response, HttpServletResponse.SC_BAD_REQUEST,
                    new ApiMessage("Specifica l'id della categoria da eliminare."));
            return;
        }

        Integer id = parseId(idParam, response);
        if (id == null) {
            return;
        }

        boolean deleted = categoryDao.delete(id);
        if (!deleted) {
            writeJson(response, HttpServletResponse.SC_NOT_FOUND,
                    new ApiMessage("Categoria non trovata."));
            return;
        }

        writeJson(response, HttpServletResponse.SC_OK,
                new ApiMessage("Categoria eliminata con successo."));
    }

    private CategoryPayload readPayload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        try (Reader reader = request.getReader()) {
            CategoryPayload payload = gson.fromJson(reader, CategoryPayload.class);
            if (payload == null
                    || payload.getCategoryName() == null
                    || payload.getCategoryName().isBlank()) {
                writeJson(response, HttpServletResponse.SC_BAD_REQUEST,
                        new ApiMessage("Il campo CategoryName e' obbligatorio."));
                return null;
            }

            payload.setCategoryName(payload.getCategoryName().trim());
            if (payload.getDescription() != null) {
                payload.setDescription(payload.getDescription().trim());
            }
            return payload;
        } catch (JsonSyntaxException ex) {
            writeJson(response, HttpServletResponse.SC_BAD_REQUEST,
                    new ApiMessage("JSON non valido."));
            return null;
        }
    }

    private void writeJson(HttpServletResponse response, int status, Object body) throws IOException {
        response.setStatus(status);
        response.getWriter().write(gson.toJson(body));
    }

    private Integer parseId(String idParam, HttpServletResponse response) throws IOException {
        try {
            return Integer.valueOf(idParam);
        } catch (NumberFormatException ex) {
            writeJson(response, HttpServletResponse.SC_BAD_REQUEST,
                    new ApiMessage("L'id deve essere un numero intero."));
            return null;
        }
    }

    private record ApiMessage(String message) {
    }

    private record CategoryView(Integer categoryId, String categoryName, String description) {

        private static CategoryView fromEntity(Category category) {
            return new CategoryView(
                    category.getCategoryId(),
                    category.getCategoryName(),
                    category.getDescription()
            );
        }
    }
}
