package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Notice;
import ezen.team.ezenbookstore.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService implements NoticeServiceInterface{

    private final NoticeRepository noticeRepository;

    private final FileUploadService fileUploadService;

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
    @Override
    public Page<Notice> getFilteredNotices(String searchType, String keyword, Pageable pageable) {
        if ("title".equalsIgnoreCase(searchType)) {
            return noticeRepository.findByTitleContaining(keyword, pageable);
        } else if ("content".equalsIgnoreCase(searchType)) {
            return noticeRepository.findByContentContaining(keyword, pageable);
        } else {
            return noticeRepository.findAll(pageable);
        }
    }

    @Override
    public Notice createNoticeWithFile(String title, String content, MultipartFile image) throws Exception {
        Notice notice = Notice.builder()
                .title(title)
                .content(content)
                .build();

        Notice savedNotice = noticeRepository.save(notice);

        if (image != null && !image.isEmpty()) {
            boolean uploadSuccess = fileUploadService.uploadFile(image, savedNotice.getId().toString(), "notice");
            if (!uploadSuccess) {
                throw new Exception("파일 업로드 실패");
            }
        }

        return savedNotice;
    }

    @Override
    public void updateNoticeWithFile(Long id, String title, String content, MultipartFile image) throws Exception {
        Notice notice = noticeRepository.findById(id).orElseThrow(() -> new Exception("공지사항을 찾을 수 없습니다."));

        notice.setTitle(title);
        notice.setContent(content);

        if (image != null && !image.isEmpty()) {
            boolean uploadSuccess = fileUploadService.uploadFile(image, id.toString(), "notice");
            if (!uploadSuccess) {
                throw new Exception("파일 업로드 실패");
            }
        }

        noticeRepository.save(notice);
    }

    @Override
    public void deleteNotices(List<Long> ids) {
        ids.forEach(noticeRepository::deleteById);
    }

}
