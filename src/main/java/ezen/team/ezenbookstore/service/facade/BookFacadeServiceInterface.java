package ezen.team.ezenbookstore.service.facade;

import ezen.team.ezenbookstore.dto.RecentBookCookieDto;
import ezen.team.ezenbookstore.entity.Book;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface BookFacadeServiceInterface {

    Page<Book> getBooks(String sort, String direction, int page, String ifkr, String category, String subcategory);

    List<String> getImageList(List<Book> books);

    Page<Book> searchBooks(String keyword, String val, String ifkr, String category, String subcategory, String sort, String direction, int page);

    String getBookImagePath(Long bookId);

    List<String> getReviewImagePathList(Long bookId);

    void recentBookCookie(Long bookId, HttpServletResponse response);

    List<RecentBookCookieDto> getRecentBookCookies(HttpServletRequest request);
}
