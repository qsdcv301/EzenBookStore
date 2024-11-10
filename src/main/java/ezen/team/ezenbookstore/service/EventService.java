package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Event;
import ezen.team.ezenbookstore.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService{

    private final EventRepository eventRepository;

    public Event findById(Long id){
        return eventRepository.findById(id).orElse(null);
    }

    public List<Event> findAll(){
        return eventRepository.findAll();
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

}
