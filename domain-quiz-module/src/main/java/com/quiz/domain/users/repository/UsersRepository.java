package com.quiz.domain.users.repository;

import com.quiz.domain.users.dto.UserNameDto;
import com.quiz.domain.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);

    Optional<Users> findByRefreshToken(String refreshToken);

    @Query("SELECT " +
            "new com.quiz.domain.users.dto.UserNameDto(" +
            "u.id, u.name) " +
            "FROM Users u " +
            "WHERE u.id in :userIds")
    List<UserNameDto> findUsernameByIds(@Param("userIds") List<Long> userIds);
}
