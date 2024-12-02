package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Event;
import ezen.team.ezenbookstore.entity.Notice;
import ezen.team.ezenbookstore.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService implements EventServiceInterface{

    private final EventRepository eventRepository;

    @Override
    public Event findById(Long id){
        return eventRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Event> findAll(Pageable pageable){
        return eventRepository.findAll(pageable);
    }

    @Override
    public Event create(Event event){
        return eventRepository.save(event);
    }

    @Override
    public Event update(Event event){
        Event newEvent = Event.builder()
                .id(event.getId())
                .title(event.getTitle())
                .content(event.getContent())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .build();
        return eventRepository.save(newEvent);
    }

    @Override
    public void delete(Long id){
        eventRepository.deleteById(id);
    }

    // 시작 전 이벤트 조회
    @Override
    public Page<Event> findUpcomingEvents(Pageable pageable) {
        return eventRepository.findByStartDateAfter(new Timestamp(System.currentTimeMillis()).toLocalDateTime(), pageable);
    }

    // 진행 중 이벤트 조회
    @Override
    public Page<Event> findOngoingEvents(Pageable pageable) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return eventRepository.findByStartDateBeforeAndEndDateAfter(now.toLocalDateTime(), now.toLocalDateTime(), pageable);
    }

    // 종료된 이벤트 조회
    @Override
    public Page<Event> findEndedEvents(Pageable pageable) {
        return eventRepository.findByEndDateBefore(new Timestamp(System.currentTimeMillis()).toLocalDateTime(), pageable);
    }

    @Override
    public List<Event> findOngoingEventList(){
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return eventRepository.findByStartDateBeforeAndEndDateAfter(now.toLocalDateTime(), now.toLocalDateTime());

    }

    @Override
    public Page<Event> searchByTitle(String keyword, Pageable pageable) {
        return eventRepository.findByTitleContaining(keyword, pageable);
    }

    @Override
    public Page<Event> searchByContent(String keyword, Pageable pageable) {
        return eventRepository.findByContentContaining(keyword, pageable);
    }

    @Override
    public String calculateEventStatus(LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(startDate)) {
            return "시작 전";
        } else if (now.isAfter(endDate)) {
            return "종료";
        } else {
            return "진행 중";
        }
    }

//    public Page<Event> calculateEventStatus() {
//        return eventRepository.;
//    }
}
