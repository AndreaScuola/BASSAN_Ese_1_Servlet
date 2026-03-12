package org.example;

import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/utenti/*")
public class Servlet1 extends HttpServlet {
    private Gson gson = new Gson();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String path = req.getPathInfo();

        try{
            // /utenti
            if(path == null || path.equals("/")){
                List<Utente> list = Database.getInstance().selectAll();
                resp.getWriter().print(gson.toJson(list));
                return;
            }

            String[] parts = path.split("/");

            if(parts[1] == "nome"){
                String nome = parts[2];
                Utente u2 = Database.getInstance().selectByName(nome);
                resp.getWriter().print(gson.toJson(u2));
                return;
            }

        }catch(Exception e){
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}