package ezen.team.ezenbookstore.repository;

import ezen.team.ezenbookstore.entity.Event;
import ezen.team.ezenbookstore.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAll(Pageable pageable);

    Page<Event> findByTitleContaining(String title, Pageable pageable);
    Page<Event> findByContentContaining(String content, Pageable pageable);
    // 시작 전 이벤트 조회
    Page<Event> findByStartDateAfter(LocalDateTime now, Pageable pageable);

    // 진행 중 이벤트 조회
    Page<Event> findByStartDateBeforeAndEndDateAfter(LocalDateTime nowStart, LocalDateTime nowEnd, Pageable pageable);

    // 종료된 이벤트 조회
    Page<Event> findByEndDateBefore(LocalDateTime now, Pageable pageable);

    List<Event> findByStartDateBeforeAndEndDateAfter(LocalDateTime nowStart, LocalDateTime nowEnd);

}
