package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoticeServiceInterface {

    Notice findById(Long id);

    Page<Notice> findAll(Pageable pageable);

    Notice create(Notice notice);

    Notice update(Notice notice);

    void delete(Long id);

    Page<Notice> findById(Long id, Pageable pageable);

    Page<Notice> searchByTitle(String keyword,Pageable pageable);

    Page<Notice> searchByContent(String keyword,Pageable pageable);

    Page<Notice> searchByTitleAndContent(String keyword, Pageable pageable);

    List<Notice> findTop5ByOrderByIdDesc();

    List<Long> noticeIds();
    
}
