package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Delivery;

import java.util.List;

public interface DeliveryServiceInterface {

    Delivery findById(Long id);

    List<Delivery> findAll();

    Delivery create(Delivery delivery);

    Delivery update(Long id, Delivery delivery);

    void delete(Long id);

}
