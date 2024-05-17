package com.backend.repository;

import com.backend.entity.Odontologo;

import java.util.List;

public interface IDao<T> {
    T guardar(T t);



    List<T> listarTodos();

}