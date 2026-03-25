package org.example;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static Database instance;
    private static Connection connection;
    private final String url = "jdbc:mysql://localhost:3306/db_servlet1";
    private final String user = "root";
    private final String password = "";

    private Database() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        this.connection = DriverManager.getConnection(url, user, password);
    }

    public static Database getInstance() throws SQLException {
        if (instance == null)
            instance = new Database();
        return instance;
    }

    public List<Utente> selectAll(){
        List<Utente> utenti = new ArrayList<>();
        String query = "SELECT * FROM utenti";

        try(PreparedStatement statement = connection.prepareStatement(query)){
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                String nome = rs.getString("nome");
                String email = rs.getString("email");
                Utente u = new Utente(nome, email);

                utenti.add(u);
            }

        }catch(SQLException e){
            System.err.println("Errore query: " + e.getMessage());
            return null;
        }

        return utenti;
    }

    public Utente selectByName(String nome){
        String query = "SELECT * FROM utenti WHERE nome = ?";

        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, nome);
            ResultSet rs = statement.executeQuery();

            if(rs.next()){
                String nomeRs = rs.getString("nome");
                String email = rs.getString("email");
                Utente u = new Utente(nomeRs, email);

                return u;
            }

        }catch(SQLException e){
            System.err.println("Errore query: " + e.getMessage());
        }

        return null;
    }
}