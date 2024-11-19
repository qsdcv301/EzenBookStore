package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Event;
import ezen.team.ezenbookstore.entity.Notice;
import ezen.team.ezenbookstore.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService{

    private final EventRepository eventRepository;

    public Event findById(Long id){
        return eventRepository.findById(id).orElse(null);
    }

    public Page<Event> findAll(Pageable pageable){
        return eventRepository.findAll(pageable);
    }

    public Event create(Event event){
        return eventRepository.save(event);
    }

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

    public void delete(Long id){
        eventRepository.deleteById(id);
    }
    public Page<Event> searchByTitle(String keyword, Pageable pageable) {
        return eventRepository.findByTitleContaining(keyword,pageable);
    }

    public Page<Event> searchByContent(String keyword,Pageable pageable) {
        return eventRepository.findByContentContaining(keyword,pageable);
    }
}
