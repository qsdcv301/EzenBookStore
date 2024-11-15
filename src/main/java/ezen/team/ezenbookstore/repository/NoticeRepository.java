package ezen.team.ezenbookstore.repository;

import ezen.team.ezenbookstore.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Optional<Notice> findByTitle(String title);
    List<Notice> findByContentContaining(String content);
    List<Notice> findAllByOrderByIdDesc();
}
