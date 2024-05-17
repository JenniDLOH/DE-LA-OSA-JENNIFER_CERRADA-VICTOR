package com.backend.service;

import com.backend.entity.Odontologo;
import com.backend.service.impl.OdontologoService;

import java.util.List;

public interface IOdontologoService {

    Odontologo registraOdontologo(Odontologo odontologo);
    List<Odontologo> listarOdontologos();


}
