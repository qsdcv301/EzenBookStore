package ezen.team.ezenbookstore.repository;

import ezen.team.ezenbookstore.entity.Book;
import ezen.team.ezenbookstore.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitleContaining(String title);

    List<Book> findByAuthorContaining(String author);

    List<Book> findByPublisherContaining(String publisher);

    List<Book> findByIsbnContaining(String isbn);

    Page<Book> findAll(Pageable pageable);

    Optional<Book> findById(Long id);

    // 국내/국외 구분 필터링 메서드
    List<Book> findAllByIfkr(Byte ifkr);

    List<Book> findAllByCategoryId(Long categoryId);

    List<Book> findAllBySubcategoryId(Long subCategoryId);

    Page<Book> findAllByIfkrAndCategoryNameAndSubcategoryName(Byte ifkr, String categoryName, String subCategoryName, Pageable pageable);

    Page<Book> findAllByIfkrAndCategoryName(Byte ifkr, String categoryName, Pageable pageable);

    Page<Book> findAllByIfkr(Byte ifkr, Pageable pageable);

    Optional<Book> findByTitle(String title);

    List<Book> findTop8ByOrderByCountDesc();

    List<Book> findTop8ByOrderByPublishDateDesc();

    @Modifying
    @Query("UPDATE Book b SET b.discount = :discount WHERE b.id = :id")
    int updateDiscountById(@Param("id") Long id, @Param("discount") int discount);
}
