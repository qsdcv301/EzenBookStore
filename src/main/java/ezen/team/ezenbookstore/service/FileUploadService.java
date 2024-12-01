package ezen.team.ezenbookstore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * 파일 업로드 메서드
     * create / update 활용 가능, 기존 파일이 있다면 삭제 후 업로드
     * @param file 업르할 이미지 파일
     * @param id 생성된 타입의 id값
     * @param type 업로드 될 파일의 타입 예) 책=book, 리뷰=review, 공지사항=notice, 이벤트=event
     * @return boolean 타입으로 생성되었는지 안되었는지 확인 가능
     * @exception IOException 입출력에 예외 발생
     * @see ezen.team.ezenbookstore.service.FileUploadService#findImageFilePath(Long, String)
     */
    public boolean uploadFile(MultipartFile file, String id, String type) {
        // 선택된 파일이 있는지 확인
        if (file.isEmpty()) {
            return false;
        }
        try {
            // 저장 타입 이용해 하위 디렉토리 경로 생성
            String userDir = uploadDir + File.separator + type;
            File directory = new File(userDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // 기존 동일한 ID로 시작하는 파일 삭제
            File[] existingFiles = directory.listFiles((dir1, name) -> name.startsWith(id + "_"));
            if (existingFiles != null && existingFiles.length > 0) {
                for (File existingFile : existingFiles) {
                    existingFile.delete();
                }
            }

            // 새로운 파일 생성
            String fileName = id + '_' + file.getOriginalFilename();
            File destinationFile = new File(directory + File.separator + fileName);
            file.transferTo(destinationFile);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public String findImageFilePath(Long id, String type) {
        // 실제 파일 시스템 경로를 사용
        String folderPath = uploadDir + "/" + type + "/";
        File dir = new File(folderPath);
        File[] matchingFiles = dir.listFiles((dir1, name) -> name.startsWith(id + "_"));

        // 파일이 존재하면 웹 경로로 반환
        return (matchingFiles != null && matchingFiles.length > 0)
                ? "/images/" + type + "/" + matchingFiles[0].getName()
                : null;
    }

    public int getImageCount(Long id, String type) {
        String folderPath = uploadDir + "/" + type + "/";
        File dir = new File(folderPath);
        File[] matchingFiles = dir.listFiles((dir1, name) -> name.startsWith(id + "_"));

        return matchingFiles != null ? matchingFiles.length : 0;
    }

}