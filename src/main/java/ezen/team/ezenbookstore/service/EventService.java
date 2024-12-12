package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Event;
import ezen.team.ezenbookstore.entity.Notice;
import ezen.team.ezenbookstore.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ezen.team.ezenbookstore.service.FileUploadService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService implements EventServiceInterface{

    private final EventRepository eventRepository;
    private final FileUploadService fileUploadService;

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

    @Override
    public Page<Event> getFilteredEvents(String searchType, String keyword, String filter, Pageable pageable) {
        Page<Event> eventPage = findAll(pageable);

        if ("title".equalsIgnoreCase(searchType)) {
            eventPage = searchByTitle(keyword, pageable);
        } else if ("content".equalsIgnoreCase(searchType)) {
            eventPage = searchByContent(keyword, pageable);
        }

        switch (filter) {
            case "upcoming":
                eventPage = findUpcomingEvents(pageable);
                break;
            case "ongoing":
                eventPage = findOngoingEvents(pageable);
                break;
            case "ended":
                eventPage = findEndedEvents(pageable);
                break;
        }

        return eventPage;
    }

    @Override
    public void updateEvent(Long id, String title, String content, LocalDateTime startDate, LocalDateTime endDate, MultipartFile file) throws Exception {
        Event event = findById(id);
        if (event == null) {
            throw new Exception("이벤트를 찾을 수 없습니다.");
        }

        event.setTitle(title);
        event.setContent(content);
        event.setStartDate(Timestamp.valueOf(startDate));
        event.setEndDate(Timestamp.valueOf(endDate));

        if (file != null && !file.isEmpty()) {
            boolean uploadSuccess = fileUploadService.uploadFile(file, id.toString(), "event");
            if (!uploadSuccess) {
                throw new Exception("파일 업로드 실패");
            }
        }

        eventRepository.save(event);
    }

    @Override
    public Map<String, Object> getEventDetail(Long id) throws Exception {
        Event event = findById(id);
        if (event == null) {
            throw new Exception("이벤트를 찾을 수 없습니다.");
        }

        Map<String, Object> eventData = new HashMap<>();
        eventData.put("id", event.getId());
        eventData.put("title", event.getTitle());
        eventData.put("content", event.getContent());
        eventData.put("startDate", event.getStartDate().toString());
        eventData.put("endDate", event.getEndDate().toString());

        String imagePath = fileUploadService.findImageFilePath(event.getId(), "event");
        eventData.put("imagePath", imagePath != null ? imagePath : "/images/default.png");

        return eventData;
    }
    @Override
    public List<Integer> getImageCounts(List<Event> events) {
        return events.stream()
                .map(event -> fileUploadService.getImageCount(event.getId(), "event"))
                .collect(Collectors.toList());
    }
//    @Override
//    public Event creatEvent(String title,String content,Timestamp startDate, Timestamp  endDate,)
}
