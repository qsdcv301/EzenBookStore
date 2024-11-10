package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.BookDescription;
import ezen.team.ezenbookstore.repository.BookDescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookDescriptionService {

    private final BookDescriptionRepository bookDescriptionRepository;

    public BookDescription findById(Long id) {
        return bookDescriptionRepository.findById(id).orElse(null);
    }

    public List<BookDescription> findAll() {
        return bookDescriptionRepository.findAll();
    }

    public BookDescription create(BookDescription bookDescription) {
        return bookDescriptionRepository.save(bookDescription);
    }

    public BookDescription update(BookDescription bookDescription) {
        BookDescription newBookDescription = BookDescription.builder()
                .id(bookDescription.getId())
                .bookId(bookDescription.getBookId())
                .book(bookDescription.getBook())
                .writer(bookDescription.getWriter())
                .contents(bookDescription.getContents())
                .build();
        return bookDescriptionRepository.save(newBookDescription);
    }

    public void delete(Long id) {
        bookDescriptionRepository.deleteById(id);
    }

}
