package ezen.team.ezenbookstore.repository;

import ezen.team.ezenbookstore.entity.ExchangeReturn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeReturnRepository extends JpaRepository<ExchangeReturn, Long> {

    List<ExchangeReturn> findAllByUserId(Long userId);

    Page<ExchangeReturn> findAllByUserId(Long userId, Pageable pageable);

    Page<ExchangeReturn> findAllByUserIdAndCategory(Long userId, Byte category, Pageable pageable);

}
