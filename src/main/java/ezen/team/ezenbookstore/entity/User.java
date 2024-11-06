package ezen.team.ezenbookstore.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Table(name = "user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "last_login")
    @CreationTimestamp
    private Timestamp lastLogin;

    @Builder
    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public User update(String name){
        this.name = name;
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    // 계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        // 확인 로직
        return true;
    }

    // 계쩡 잠금 여부
    @Override
    public boolean isAccountNonLocked() {
        // 확인 로직
        return true;
    }

    // 패스워드의 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
        // 확인 로직
        return true;
    }

    // 계정 사용 가능 여부
    @Override
    public boolean isEnabled() {
        // 확인 로직
        return true;
    }
}