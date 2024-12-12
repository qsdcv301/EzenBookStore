package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.dto.BookDescriptionDto;
import ezen.team.ezenbookstore.entity.BookDescription;
import ezen.team.ezenbookstore.repository.BookDescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public BookDescriptionDto formatDescriptionElements(Long id) {
        // BookDescription 조회
        BookDescription bookDescription = bookDescriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 책 설명을 찾을 수 없습니다: " + id));

        // description, writer, contents 각각 줄바꿈 처리 후 DTO에 담기
        BookDescriptionDto dto = new BookDescriptionDto();
        dto.setId(bookDescription.getId());
        dto.setDescription(formatText(bookDescription.getDescription()));
        dto.setWriter(formatText(bookDescription.getWriter()));
        dto.setContents(formatText(bookDescription.getContents()));

        return dto;
    }

    private String formatText(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.replace("\n", "<br>");
    }
}
