package ezen.team.ezenbookstore.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadServiceInterface {

    /**
     * 파일 업로드 메서드
     * create / update 활용 가능, 기존 파일이 있다면 삭제 후 업로드
     * @param file 업르할 이미지 파일
     * @param id 생성된 타입의 id값
     * @param type 업로드 될 파일의 타입 예) 책=book, 리뷰=review, 공지사항=notice, 이벤트=event
     * @return boolean 타입으로 생성되었는지 안되었는지 확인 가능
     * @exception IOException 입출력에 예외 발생
     */
    boolean uploadFile(MultipartFile file, String id, String type);

    String findImageFilePath(Long id, String type);

    int getImageCount(Long id, String type);

}
