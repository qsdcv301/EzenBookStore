package ezen.team.ezenbookstore.repository;

import ezen.team.ezenbookstore.entity.Event;
import ezen.team.ezenbookstore.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAll(Pageable pageable);

    Page<Event> findByTitleContaining(String keyword,Pageable pageable);

    Page<Event> findByContentContaining(String keyword,Pageable pageable);
}
