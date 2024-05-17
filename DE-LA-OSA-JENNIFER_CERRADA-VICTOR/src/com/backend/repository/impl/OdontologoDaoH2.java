package com.backend.repository.impl;
import java.sql.*;
import com.backend.entity.Odontologo;
import com.backend.repository.IDao;
import com.backend.dbconnection.H2Connection;
import org.apache.log4j.Logger;


import java.util.ArrayList;
import java.util.List;

public class OdontologoDaoH2 implements IDao <Odontologo> {
    private final Logger LOGGER = Logger.getLogger(OdontologoDaoH2.class);



    @Override
    public Odontologo guardar(Odontologo odontologo) {
        Connection connection = null;
        Odontologo odontologoGuardado = null;


        try {
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ODONTOLOGOS (MATRICULA, NOMBRE, APELLIDO) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, odontologo.getMatricula());
            preparedStatement.setString(2, odontologo.getNombre());
            preparedStatement.setString(3, odontologo.getApellido());
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            odontologoGuardado = new Odontologo(odontologo.getMatricula(), odontologo.getNombre(), odontologo.getApellido());
            while (resultSet.next()) {
                odontologoGuardado.setId(resultSet.getLong("id"));
            }
            connection.commit();
            LOGGER.info("Se ha registrado el Odontólogo: " + odontologoGuardado);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                    LOGGER.info("Ocurrio un Problema");
                    LOGGER.error(e.getMessage());
                    e.printStackTrace();
                } catch (SQLException exception) {
                    LOGGER.error(exception.getMessage());
                    exception.printStackTrace();
                }
            }
        } finally {
            try {
                connection.close();
            } catch (Exception ex) {
                LOGGER.error("No se pudo cerrar la conexion: " + ex.getMessage());
            }
        }

        return odontologoGuardado;
    }



    @Override
    public List<Odontologo> listarTodos() {
        Connection connection = null;
        List<Odontologo> odontologos = new ArrayList<>();

        try{
            connection = H2Connection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ODONTOLOGOS");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Odontologo odontologo = new Odontologo(resultSet.getLong("id"), resultSet.getString("matricula"), resultSet.getString("nombre"),resultSet.getString("apellido"));

                odontologos.add(odontologo);
            }



        }catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();

        } finally {
            try {
                connection.close();
            } catch (Exception ex) {
                LOGGER.error("Ha ocurrido un error al intentar cerrar la base de datos. " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        LOGGER.info("Listado de Odontólogos obtenido: " + odontologos);

        return odontologos;
    }
}
