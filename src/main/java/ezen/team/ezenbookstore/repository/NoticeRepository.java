package ezen.team.ezenbookstore.repository;

import ezen.team.ezenbookstore.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Page<Notice> findAll(Pageable pageable);
    Page<Notice> findById(Long id, Pageable pageable);
    Page<Notice> findByTitleContaining(String keyword,Pageable pageable);

    Page<Notice> findByContentContaining(String keyword,Pageable pageable);

    Page<Notice> findByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword, Pageable pageable);

    List<Notice> findTop5ByOrderByIdDesc();

}
