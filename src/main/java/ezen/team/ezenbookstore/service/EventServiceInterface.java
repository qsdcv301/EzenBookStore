package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface EventServiceInterface {

    Event findById(Long id);

    Page<Event> findAll(Pageable pageable);

    Event create(Event event);

    Event update(Event event);

    void delete(Long id);

    Page<Event> findUpcomingEvents(Pageable pageable);

    Page<Event> findOngoingEvents(Pageable pageable);

    Page<Event> findEndedEvents(Pageable pageable);

    List<Event> findOngoingEventList();

    Page<Event> searchByTitle(String keyword, Pageable pageable);

    Page<Event> searchByContent(String keyword, Pageable pageable);

    String calculateEventStatus(LocalDateTime startDate, LocalDateTime endDate);

    Page<Event> getFilteredEvents(String searchType, String keyword, String filter, Pageable pageable);

    void updateEvent(Long id, String title, String content, LocalDateTime startDate, LocalDateTime endDate, MultipartFile file) throws Exception;

    Map<String, Object> getEventDetail(Long id) throws Exception;

    List<Integer> getImageCounts(List<Event> events);

    Event createEventWithFile(String title, String content, LocalDateTime startDate, LocalDateTime endDate, MultipartFile image) throws Exception;
}
