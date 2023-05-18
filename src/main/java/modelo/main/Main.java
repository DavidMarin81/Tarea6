package modelo.main;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import modelo.Alumno;
import modelo.DBBeanReceptor;
import modelo.dao.alumno.AlumnoMySQLDao;
import modelo.dao.alumno.IAlumnoDao;

public class Main {

	public static void main(String[] args) {
	//Creamos entidad alumno
		Alumno alumno = new Alumno();
		alumno.setApellidos("Marin Gomez");
		alumno.setDNI("53170229W");

		LocalDate localDate = LocalDate.of(1981, 7, 7);
		Date date = Date.valueOf(localDate);
		alumno.setFechaNac(date);
		alumno.setDireccion("Vigo");

		alumno.setNombre("David");
		
		//Creamos DAO
		IAlumnoDao alumnoMySQLDao = new AlumnoMySQLDao();
		
		System.out.println("Antes de insertar: --------------------");
		//Mostramos alumnos existentes en BD
		showList(alumnoMySQLDao.findAll());
		
		//Creamos un Bean receptor
		DBBeanReceptor rec = new DBBeanReceptor();
		//Añadimos el bean receptor como oyente del evento BDModificadaEvent
		((AlumnoMySQLDao)alumnoMySQLDao).addBDModificadaListener(rec);
		
		//Creamos un nuevo registro en BD. Esto disparará el evento personalizado y se avisará al bean oyente
		alumnoMySQLDao.create(alumno);
		
		//Comprobamos si se ha insertado.
		System.out.println("Después de insertar: --------------------");
		showList(alumnoMySQLDao.findAll());
	}
	
	private static void showList(List<Alumno> alumnos) {
		for (Alumno alumno : alumnos) {
			System.out.println(alumno);
		}
	}

}
