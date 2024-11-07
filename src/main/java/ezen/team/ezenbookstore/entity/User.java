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

    @Column(name = "tel")
    private String tel;

    @Column(name = "addr")
    private String addr;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "birthday")
    private Integer birthday;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "point")
    private Integer point;

    @Builder
    public void Builder(Long id, String email, String name, String password, String tel, String addr, Integer grade, Integer point) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.tel = tel;
        this.addr = addr;
        this.grade = grade;
        this.point = point;
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