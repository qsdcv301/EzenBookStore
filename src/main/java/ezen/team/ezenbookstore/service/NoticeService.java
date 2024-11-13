package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Notice;
import ezen.team.ezenbookstore.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public Notice findById(Long id) {
        return noticeRepository.findById(id).orElse(null);
    }

    public List<Notice> findAll() {
        return noticeRepository.findAll();
    }

    public Notice create(Notice notice) {
        return noticeRepository.save(notice);
    }

    public Notice update(Notice notice) {
        Notice updateNotice = Notice.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createAt(notice.getCreateAt())
                .build();
        return noticeRepository.save(updateNotice);
    }

    public void delete(Long id) {
        noticeRepository.deleteById(id);
    }

}
