package ezen.team.ezenbookstore.entity;

import ezen.team.ezenbookstore.service.UserService;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Builder(toBuilder = true)
@Table(name = "user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "provider")
    private String provider;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "tel")
    private String tel;

    @Column(name = "addr")
    private String addr;

    @Column(name = "addrextra")
    private String addrextra;

    @Column(name = "create_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "birthday")
    private Timestamp birthday;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "point")
    private Long point;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
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