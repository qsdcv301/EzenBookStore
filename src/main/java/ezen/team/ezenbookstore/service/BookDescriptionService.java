package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.BookDescription;
import ezen.team.ezenbookstore.repository.BookDescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookDescriptionService implements BookDescriptionServiceInterface{

    private final BookDescriptionRepository bookDescriptionRepository;

    @Override
    public BookDescription findById(Long id) {
        return bookDescriptionRepository.findById(id).orElse(null);
    }

    @Override
    public List<BookDescription> findAll() {
        return bookDescriptionRepository.findAll();
    }

    @Override
    public BookDescription create(BookDescription bookDescription) {
        return bookDescriptionRepository.save(bookDescription);
    }

    @Override
    public BookDescription update(BookDescription bookDescription) {
        BookDescription newBookDescription = BookDescription.builder()
                .id(bookDescription.getId())
                .description(bookDescription.getDescription())
                .writer(bookDescription.getWriter())
                .contents(bookDescription.getContents())
                .build();
        return bookDescriptionRepository.save(newBookDescription);
    }

    @Override
    public void delete(Long id) {
        bookDescriptionRepository.deleteById(id);
    }

}
