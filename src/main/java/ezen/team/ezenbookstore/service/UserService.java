package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.User;
import ezen.team.ezenbookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(User user) {
        user.setEmail(user.getEmail());
        user.setProvider(user.getProvider());
        user.setProviderId(user.getProviderId());
        user.setName(user.getName());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user).getId();
    }
}
