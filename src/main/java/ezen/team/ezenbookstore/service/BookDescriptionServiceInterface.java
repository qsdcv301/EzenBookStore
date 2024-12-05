package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.BookDescription;

import java.util.List;

public interface BookDescriptionServiceInterface {

    BookDescription findById(Long id);

    List<BookDescription> findAll();

    BookDescription create(BookDescription bookDescription);

    BookDescription update(BookDescription bookDescription);

    void delete(Long id);

}
