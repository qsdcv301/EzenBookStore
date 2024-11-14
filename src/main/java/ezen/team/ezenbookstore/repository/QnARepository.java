package ezen.team.ezenbookstore.repository;

import ezen.team.ezenbookstore.entity.QnA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QnARepository extends JpaRepository<QnA, Long> {
    List<QnA> findAllByUserId(Long userId);
}
