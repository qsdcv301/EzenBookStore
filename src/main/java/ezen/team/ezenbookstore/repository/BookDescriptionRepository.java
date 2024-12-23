package ezen.team.ezenbookstore.repository;

import ezen.team.ezenbookstore.entity.BookDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookDescriptionRepository extends JpaRepository<BookDescription, Long> {
}
