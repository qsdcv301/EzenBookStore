package ezen.team.ezenbookstore.repository;

import ezen.team.ezenbookstore.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndProvider(String email, String provider);

    Optional<User> findByNameAndTel(String name, String tel);

    Optional<User> findByEmailAndTel(String email, String tel);

    Page<User> findByGrade(Integer grade, Pageable pageable);
}
