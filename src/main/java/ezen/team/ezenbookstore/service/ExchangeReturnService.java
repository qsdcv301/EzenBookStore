package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.ExchangeReturn;
import ezen.team.ezenbookstore.repository.ExchangeReturnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeReturnService {

    private final ExchangeReturnRepository exchangeReturnRepository;

    public ExchangeReturn findById(Long id) {
        return exchangeReturnRepository.findById(id).orElse(null);
    }

    public List<ExchangeReturn> findAll() {
        return exchangeReturnRepository.findAll();
    }

    public ExchangeReturn create(ExchangeReturn exchangeReturn) {
        return exchangeReturnRepository.save(exchangeReturn);
    }

    public ExchangeReturn update(ExchangeReturn exchangeReturn) {
        ExchangeReturn newExchangeReturn = ExchangeReturn.builder()
                .id(exchangeReturn.getId())
                .user(exchangeReturn.getUser())
                .category(exchangeReturn.getCategory())
                .question(exchangeReturn.getQuestion())
                .answer(exchangeReturn.getAnswer())
                .createAt(exchangeReturn.getCreateAt())
                .build();
        return exchangeReturnRepository.save(newExchangeReturn);
    }

    public void deleteById(Long id) {
        exchangeReturnRepository.deleteById(id);
    }

    public List<ExchangeReturn> findAllByUserId(Long userId) {
        return exchangeReturnRepository.findAllByUserId(userId);
    }

    public Page<ExchangeReturn> findAll(Pageable pageable) {
        return exchangeReturnRepository.findAll(pageable);
    }

    public Page<ExchangeReturn> findAllByUserId(Long userId, Pageable pageable) {
        return exchangeReturnRepository.findAllByUserId(userId, pageable);
    }

    public Page<ExchangeReturn> findAllByUserIdAndCategory(Long userId, Byte category, Pageable pageable) {
        return exchangeReturnRepository.findAllByUserIdAndCategory(userId, category, pageable);
    }

}
