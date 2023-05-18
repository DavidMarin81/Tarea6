/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao.alumno;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import modelo.Alumno;
import modelo.dao.AbstractGenericDao;
import modelo.events.BDModificadaEvent;
import modelo.events.BDModificadaListener;
import modelo.util.ConnectionManager;
import modelo.util.MyDataSource;

/**
 *
 * @author mfernandez
 */
public class AlumnoMySQLDao extends 
AbstractGenericDao<Alumno> implements IAlumnoDao, Serializable {

	private static final long serialVersionUID = 1L;
	
	//Se crea un objeto dataSource
	private MyDataSource dataSource;

	// Suponemos que solo habrá un receptor.
	// Para varios receptores habría que tener una lista de listeners
	private BDModificadaListener receptor;

	// Añadimos mecamismo para añadir/eliminar receptor/(es)
	public void addBDModificadaListener(BDModificadaListener receptor) {
		this.receptor = receptor;
	}

	public void removeBDModificadaListener(BDModificadaListener receptor) {
		this.receptor = null;
	}

	public AlumnoMySQLDao() {
		this.dataSource = ConnectionManager.getDataSource();
	}

	/*******************************************************
	 *
	 * @param listener
	 */

	@Override
	public boolean create(Alumno entity) {
		{
			Connection con = null;
			
			boolean exito = false;
			try {
				//Punto D -> Preparamos la conexion 
				con = DriverManager.getConnection(this.dataSource.getUrl(), this.dataSource.getUser(), this.dataSource.getPwd());
				con.setAutoCommit(false);
				PreparedStatement stmt = con.prepareStatement(
						"insert into alumnos(DNI, Nombre, Apellidos, Direccion, FechaNac) values (?,?,?,?,?)");

				stmt.setString(1, entity.getDNI());
				stmt.setString(2, entity.getNombre());
				stmt.setString(3, entity.getApellidos());
				stmt.setString(4, entity.getDireccion());
				stmt.setDate(5, entity.getFechaNac());

				int rowCount = stmt.executeUpdate();
				con.commit();

				// Notificamos al receptor el evento de modificación
				this.receptor.capturarBDModificada(new BDModificadaEvent(this));
				exito = (rowCount == 1);

			} catch (SQLException e) {
				if (con != null) {
					try {
						System.err.print("Transaction is being rolled back");
						con.rollback();
					} catch (SQLException excep) {
						excep.printStackTrace();
					}
				}
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return exito;

		}
	}

	@Override
	public Alumno read(long id) throws modelo.dao.exceptions.InstanceNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(Alumno entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(Alumno entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Alumno> findAll() {

		List<Alumno> alumnos = new ArrayList<>();
		Connection con = null;
		try {
			//Punto D -> Preparamos la conexion 
			con = DriverManager.getConnection(this.dataSource.getUrl(), this.dataSource.getUser(), this.dataSource.getPwd());
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery("select * from alumnos");
			while (rs.next()) {
				Alumno a = new Alumno(rs.getString("DNI"), rs.getString("Nombre"), rs.getString("Apellidos"),
						rs.getString("Direccion"), rs.getDate("FechaNac"));

				alumnos.add(a);

				//System.out.println(a);
			}

		} catch (Exception ex) {

			System.err.println("Ha ocurrido una exception: " + ex.getMessage());
		} finally {

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.err.println("Ha ocurrido una exception cerrando la conexión: " + e.getMessage());
				}
			}
		}

		return alumnos;
	}

}
