package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Notice;
import ezen.team.ezenbookstore.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public Notice findById(Long id) {
        return noticeRepository.findById(id).orElse(null);
    }

    public Page<Notice> findAll(Pageable pageable) {
        return noticeRepository.findAll(pageable);
    }

    public Notice create(Notice notice) {
        return noticeRepository.save(notice);
    }

    public Notice update(Notice notice) {
        Notice updateNotice = Notice.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createAt(findById(notice.getId()).getCreateAt())
                .build();
        return noticeRepository.save(updateNotice);
    }

    public void delete(Long id) {
        noticeRepository.deleteById(id);
    }
    public Page<Notice> searchByTitle(String keyword,Pageable pageable) {
        return noticeRepository.findByTitleContaining(keyword,pageable);
    }

    public Page<Notice> searchByContent(String keyword,Pageable pageable) {
        return noticeRepository.findByContentContaining(keyword,pageable);
    }
    public Page<Notice> searchByTitleAndContent(String keyword, Pageable pageable){
        return noticeRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
    };

    public List<Notice> findTop5ByOrderByIdDesc(){
        return noticeRepository.findTop5ByOrderByIdDesc();
    }

}
