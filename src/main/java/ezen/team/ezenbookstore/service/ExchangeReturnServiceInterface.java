package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.ExchangeReturn;
import ezen.team.ezenbookstore.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ExchangeReturnServiceInterface {

    ExchangeReturn findById(Long id);

    List<ExchangeReturn> findAll();

    ExchangeReturn create(ExchangeReturn exchangeReturn);

    ExchangeReturn update(ExchangeReturn exchangeReturn);

    void deleteById(Long id);

    List<ExchangeReturn> findAllByUserId(Long userId);

    Page<ExchangeReturn> findAll(Pageable pageable);

    Page<ExchangeReturn> findAllByUserId(Long userId, Pageable pageable);

    Page<ExchangeReturn> findAllByUserIdAndCategory(Long userId, Byte category, Pageable pageable);

    Map<String, String> findExchangeReturnDetails(String ERId);

    Map<String, Boolean> addExchangeReturn(ExchangeReturn er, long orderItemId, User user, MultipartFile file);
    
}
