package ezen.team.ezenbookstore.repository;

import ezen.team.ezenbookstore.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndProvider(String email, String provider);

    Optional<User> findByNameAndTel(String name, String tel);

    Optional<User> findByEmailAndTel(String email, String tel);

    // 이메일에 특정 문자열이 포함된 사용자 검색 (대소문자 무시)
    List<User> findByEmailContainingIgnoreCase(String email);

    // 이름에 특정 문자열이 포함된 사용자 검색 (대소문자 무시)
    List<User> findByNameContainingIgnoreCase(String name);

}
