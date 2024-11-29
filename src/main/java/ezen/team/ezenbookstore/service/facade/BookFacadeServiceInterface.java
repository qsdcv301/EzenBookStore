package ezen.team.ezenbookstore.service.facade;

import ezen.team.ezenbookstore.entity.Book;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookFacadeServiceInterface {

    Page<Book> getBooks(String sort, String direction, int page, String ifkr, String category, String subcategory);

    List<String> getImageList(List<Book> books);

    Page<Book> searchBooks(String keyword, String val, String ifkr, String category, String subcategory, String sort, String direction, int page);

    Book updateBookCount(Long bookId);

    String getBookImagePath(Long bookId);

    List<String> getReviewImagePathList(Long bookId);

}
