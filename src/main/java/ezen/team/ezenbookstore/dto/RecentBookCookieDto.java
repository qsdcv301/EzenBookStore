package ezen.team.ezenbookstore.dto;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Builder(toBuilder = true)
public class RecentBookCookieDto {
    Long id;
    String title;
    String author;
    String publisher;
    String imgPath;
}
