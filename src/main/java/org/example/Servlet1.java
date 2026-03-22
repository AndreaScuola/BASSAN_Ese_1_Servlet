package org.example;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/utenti/*")
public class Servlet1 extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");
        String path = req.getPathInfo(); // es. "/nome/Mario" o null

        try {
            // caso: /utenti oppure /utenti/
            if (path == null || path.equals("/")) {
                List<Utente> list = Database.getInstance().selectAll();
                resp.getWriter().print(gson.toJson(list));
                return;
            }

            // split path: "/nome/Mario" -> ["", "nome", "Mario"]
            String[] parts = path.split("/");

            // caso: /utenti/nome/{nome}
            if (parts.length >= 3 && "nome".equals(parts[1])) {
                String nome = parts[2];
                Utente u2 = Database.getInstance().selectByName(nome);

                if (u2 != null) {
                    resp.getWriter().print(gson.toJson(u2));
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Utente non trovato");
                }
                return;
            }

            // path non riconosciuto
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "URL non valido");

        } catch (Exception e) {
            // log dell'errore su console Tomcat
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore interno del server");
        }
    }
}