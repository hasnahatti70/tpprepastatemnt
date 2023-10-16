/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ma.projet.sevice;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ma.projet.beans.Employe;
import ma.projet.connexion.Connexion;
import ma.projet.dao.IDao;

/**
 *
 * @author LENOVO
 */
public class EmployeService implements IDao<Employe> {
@Override
public boolean create(Employe o) {
try {
String req = "insert into emp (nom, prenom) values(?,?)";
PreparedStatement ps =
Connexion.getConnection().prepareStatement(req);
ps.setString(1, o.getNom());
ps.setString(2, o.getPrenom());
if (ps.executeUpdate() == 1) {
return true;
}
} catch (SQLException ex) {
Logger.getLogger(EmployeService.class.getName()).log(Level.SEVERE,
null, ex);
}
return false;
}
@Override
public boolean update(Employe o) {
try {
String req = "update emp set nom = ? , prenom = ? where id =?";
PreparedStatement ps =Connexion.getConnection().prepareStatement(req);
ps.setString(1, o.getNom());
ps.setString(2, o.getPrenom());
ps.setInt(3, o.getId());
if (ps.executeUpdate() == 1) {
return true; 
}
} catch (SQLException ex) {
Logger.getLogger(EmployeService.class.getName()).log(Level.SEVERE,
null, ex);
}
return false;
}
@Override
public boolean delete(Employe o) {
try {
String req = "delete from emp where id = ?";
PreparedStatement ps =
Connexion.getConnection().prepareStatement(req);
ps.setInt(1, o.getId());
if (ps.executeUpdate() == 1) {
return true;
}
} catch (SQLException ex) {
Logger.getLogger(EmployeService.class.getName()).log(Level.SEVERE,
null, ex);
}
return false;
}

public Employe getById(int id) {
Employe emp = null;
try {
String req = "select * from emp where id = ?";
PreparedStatement ps =
Connexion.getConnection().prepareStatement(req);
ps.setInt(1, id);
ResultSet rs = ps.executeQuery();
if(rs.next())
emp = new Employe(rs.getInt("id"), rs.getString("nom"),rs.getString("prenom"));
} catch (SQLException ex) {
Logger.getLogger(EmployeService.class.getName()).log(Level.SEVERE,
null, ex);
}
return emp;
}

public List<Employe> getAll() {
List <Employe> emps = new ArrayList<>();
try {
String req = "select * from emp ";
PreparedStatement ps =
Connexion.getConnection().prepareStatement(req);
ResultSet rs = ps.executeQuery();
while(rs.next())
emps.add(new Employe(rs.getInt("id"),
rs.getString("nom"), rs.getString("prenom")));
} catch (SQLException ex) {
    Logger.getLogger(EmployeService.class.getName()).log(Level.SEVERE,
null, ex);
}
return emps;
}

    @Override
    public Employe findById(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Employe> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}