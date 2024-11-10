package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.User;
import ezen.team.ezenbookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User create(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User update(User user) {
        User newUser = User.builder()
                .id(user.getId())
                .provider(user.getProvider())
                .email(user.getEmail())
                .name(user.getName())
                .tel(user.getTel())
                .addr(user.getAddr())
                .addrextra(user.getAddrextra())
                .createdAt(user.getCreatedAt())
                .birthday(user.getBirthday())
                .grade(user.getGrade())
                .point(user.getPoint())
                .build();
        return userRepository.save(newUser);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public User findByEmailAndProvider(String email, String provider) {
        return userRepository.findByEmailAndProvider(email, provider);
    }

}
