package ezen.team.ezenbookstore.service;
import org.springframework.stereotype.Service;

@Service
public class TextFormatService implements TextFormatInterface{

    @Override
    public String formatText(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        //공백 처리 엔터처리
        return text.replaceAll(" ", "&nbsp;").replaceAll("\n", "<br>");
    }
}
