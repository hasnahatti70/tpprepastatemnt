/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ma.projet.beans.Employe;
import ma.projet.beans.Message;
import ma.projet.connexion.Connexion;
import ma.projet.dao.EmployeService;
import ma.projet.dao.IDao;

public class MessageService implements IDao<Message> {
    private EmployeService es;
    
    public MessageService() {
        es = new EmployeService();
    }
    
    @Override
    public boolean create(Message o) {
        try {
            String req = "insert into message (objet, sujet, date, idE, idR) values(?,?,?,?,?)";
            PreparedStatement ps = Connexion.getConnection().prepareStatement(req);
            ps.setString(1, o.getObject());
            ps.setString(2, o.getSujet());
            ps.setDate(3, new java.sql.Date(o.getDate().getTime()));
            ps.setInt(4, o.getEmpEmetteur().getId());
            ps.setInt(5, o.getEmpRecepteur().getId());
            
            if (ps.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MessageService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean update(Message o) {
        try {
            String req = "UPDATE message SET objet=?, sujet=?, date=?, idE=?, idR=? WHERE id=?";
            PreparedStatement ps = Connexion.getConnection().prepareStatement(req);
            ps.setString(1, o.getObject());
            ps.setString(2, o.getSujet());
            ps.setDate(3, new java.sql.Date(o.getDate().getTime()));
            ps.setInt(4, o.getEmpEmetteur().getId());
            ps.setInt(5, o.getEmpRecepteur().getId());
            ps.setInt(6, o.getId());
            
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            Logger.getLogger(MessageService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean delete(Message o) {
        try {
            String req = "DELETE FROM message WHERE id=?";
            PreparedStatement ps = Connexion.getConnection().prepareStatement(req);
            ps.setInt(1, o.getId());
            
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            Logger.getLogger(MessageService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public Message getById(int id) {
        Message message = null;
        try {
            String req = "SELECT * FROM message WHERE id=?";
            PreparedStatement ps = Connexion.getConnection().prepareStatement(req);
            ps.setInt(1, id);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                message = new Message();
                message.setId(rs.getInt("id"));
                message.setObject(rs.getString("objet"));
                message.setSujet(rs.getString("sujet"));
                message.setDate(rs.getDate("date"));
                
                int idEmetteur = rs.getInt("idE");
                int idRecepteur = rs.getInt("idR");
                Employe emetteur = es.getById(idEmetteur);
                Employe recepteur = es.getById(idRecepteur);
                message.setEmpEmetteur(emetteur);
                message.setEmpRecepteur(recepteur);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MessageService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return message;
    }

    public List<Message> getAll() {
        List<Message> messages = new ArrayList<>();
        try {
            String req = "SELECT * FROM message";
            Statement statement = Connexion.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(req);
            
            while (rs.next()) {
                Message message = new Message();
                message.setId(rs.getInt("id"));
                message.setObject(rs.getString("objet"));
                message.setSujet(rs.getString("sujet"));
                message.setDate(rs.getDate("date"));
                
                int idEmetteur = rs.getInt("idE");
                int idRecepteur = rs.getInt("idR");
                Employe emetteur = es.getById(idEmetteur);
                Employe recepteur = es.getById(idRecepteur);
                message.setEmpEmetteur(emetteur);
                message.setEmpRecepteur(recepteur);
                
                messages.add(message);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MessageService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return messages;
    }
}
