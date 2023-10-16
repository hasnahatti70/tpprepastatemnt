/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ma.service;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ma.beans.Client;
import ma.beans.Service;
import ma.connexion.Connexion;
import ma.dao.IDao;

/**
 *
 * @author Lachgar
 */
public class ClientService implements IDao<Client>{
    private ServiceService ss;

    public ClientService() {
        ss = new ServiceService();
    }
    

    @Override
    public boolean create(Client o) {
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String req = "insert into client values (null, ?, ?, ?, ?)";
            PreparedStatement ps = Connexion.getConnection().prepareStatement(req);
            ps.setString(1, o.getNom());
            ps.setString(2, o.getPrenom());
            ps.setDate(3, new Date(o.getDate().getTime()));
            ps.setInt(4, o.getService().getId());
            if(ps.executeUpdate() == 1)
                return true;
        } catch (SQLException ex) {
            System.out.println("Erreur d'ajout d'un client:" +ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(Client client) {
    // Vérifiez si le client existe dans la base de données
    Client existingClient = findById(client.getId());
    
    if (existingClient == null) {
        return false; // Le client n'existe pas, donc la mise à jour échoue
    }
    
    // Mettez à jour les informations du client existant avec les nouvelles informations
    existingClient.setNom(client.getNom());
    existingClient.setPrenom(client.getPrenom());
    existingClient.setDate(client.getDate());
    existingClient.setService(client.getService());
    
    // Effectuez la mise à jour dans la base de données (votre code spécifique ici)
    
    // Retournez true si la mise à jour a réussi
    return true;
}


    @Override
    public boolean delete(Client o) {
        String req = "delete from client where id = "+ o.getId();
        try {
            Statement st = Connexion.getConnection().createStatement();
            st.executeUpdate(req);
            return true;
        } catch (SQLException ex) {
            System.out.println("Erreur de suppression d'un client:" +ex.getMessage());
        }
        return false;
    }

    @Override
    public Client findById(int id) {
        Client client = null;
        ResultSet rs = null;
        String req = "select * from client where id = "+id;
        try {
            Statement st = Connexion.getConnection().createStatement();
            rs = st.executeQuery(req);
            if (rs.next()){
              client = new Client(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getDate("date"), ss.findById(rs.getInt("service")));
            }
        } catch (SQLException ex) {
            System.out.println("Erreur findById client:" +ex.getMessage());
        }
        return client;
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        ResultSet rs = null;
        String req = "select * from client";
        try {
            Statement st = Connexion.getConnection().createStatement();
            rs = st.executeQuery(req);
            while (rs.next()){
              clients.add(new Client(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getDate("date"), ss.findById(rs.getInt("service"))));
            }
        } catch (SQLException ex) {
            System.out.println("Erreur findAll client:" +ex.getMessage());
        }
        return clients;
            
    }

  public List<Client> findByService(Service service) {
    List<Client> clients = new ArrayList<>();
    ResultSet rs = null;
    String req = "SELECT * FROM client WHERE service = ?";
    try {
        PreparedStatement ps = Connexion.getConnection().prepareStatement(req);
        ps.setInt(1, service.getId());
        rs = ps.executeQuery();
        while (rs.next()) {
            clients.add(new Client(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getDate("date"), service));
        }
    } catch (SQLException ex) {
        System.out.println("Erreur findByService client:" + ex.getMessage());
    }
    return clients;
}



    
    

    
}
