package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Notice;
import ezen.team.ezenbookstore.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService implements NoticeServiceInterface{

    private final NoticeRepository noticeRepository;

    @Override
    public Notice findById(Long id) {
        return noticeRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Notice> findAll(Pageable pageable) {
        return noticeRepository.findAll(pageable);
    }

    @Override
    public Notice create(Notice notice) {
        return noticeRepository.save(notice);
    }

    @Override
    public Notice update(Notice notice) {
        Notice updateNotice = Notice.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createAt(findById(notice.getId()).getCreateAt())
                .build();
        return noticeRepository.save(updateNotice);
    }

    @Override
    public void delete(Long id) {
        noticeRepository.deleteById(id);
    }

    @Override
    public Page<Notice> findById(Long id, Pageable pageable) {
        return noticeRepository.findById(id,pageable);
    }

    @Override
    public Page<Notice> searchByTitle(String keyword,Pageable pageable) {
        return noticeRepository.findByTitleContaining(keyword,pageable);
    }

    @Override
    public Page<Notice> searchByContent(String keyword,Pageable pageable) {
        return noticeRepository.findByContentContaining(keyword,pageable);
    }

    @Override
    public Page<Notice> searchByTitleAndContent(String keyword, Pageable pageable){
        return noticeRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
    };

    @Override
    public List<Notice> findTop5ByOrderByIdDesc(){
        return noticeRepository.findTop5ByOrderByIdDesc();
    }

    @Override
    public List<Long> noticeIds() {
        List<Notice> notices = noticeRepository.findAll();
        return notices.stream()
                .map(Notice::getId) // id만 매핑
                .collect(Collectors.toList());
    }

}
