package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface UserServiceInterface {

    Object userInConnection();

    String getUserEmail();

    User findById(Long id);

    Page<User> findAll(Pageable pageable);

    User create(User user);

    User update(User user);

    User updatePass(User user);

    void deleteById(Long id);

    User findByEmail(String email);

    User findByEmailAndProvider(String email, String provider);

    User findByNameAndTel(String name, String tel);

    User findByEmailAndTel(String email, String tel);

    List<User> findAllUsers();

    List<User> findUsersByEmail(String email);

    List<User> findUsersByName(String name);

    Long userCount();

    @Transactional
    List<User> fetchUserList(String type, String keyword);

    @Transactional
    List<User> filterUsersByGrade(List<User> userList, Integer grade);

    @Transactional
    List<User> paginateUsers(List<User> users, int page, int size);

    @Transactional
    Map<String, Long> calculateGradeCounts(List<User> userList);

    @Transactional
    int calculateTotalPages(int totalItems, int size);

}
