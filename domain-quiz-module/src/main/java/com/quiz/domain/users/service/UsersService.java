package com.quiz.domain.users.service;

import com.quiz.domain.users.dto.UserInfoDto;
import com.quiz.domain.users.dto.UserNameDto;
import com.quiz.domain.users.dto.UsersRequestDto;
import com.quiz.domain.users.entity.Users;
import com.quiz.domain.users.enums.Role;
import com.quiz.domain.users.repository.UsersRepository;
import com.quiz.exception.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.quiz.exception.code.UserErrorCode.USER_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Transactional(value = "mysqlTx")
@Service
public class UsersService {
    private final UsersRepository usersRepository;

    public void save(UsersRequestDto request) {
        Users users = Users.builder()
                .email(request.getEmail())
                .name(request.getName())
                .provider(request.getProvider())
                .build();
        usersRepository.save(users);
    }

    public Users findOrCreateUsers(UsersRequestDto request) {
        return usersRepository.findByEmail(request.getEmail())
                .orElseGet(() -> usersRepository.save(Users.builder()
                        .email(request.getEmail())
                        .name(request.getName())
                        .provider(request.getProvider())
                        .role(Role.USER)
                        .build()));
    }

    @Transactional(value = "mysqlTx", propagation = Propagation.REQUIRES_NEW)
    public List<UserNameDto> findUsernameDtosByIds(List<Long> userIds) {
        return usersRepository.findUsernameByIds(userIds);
    }

    public UserInfoDto getUserInfo(String email) {
        Optional<Users> optionalUsers = usersRepository.findByEmail(email);
        if (optionalUsers.isPresent()) {
            Users users = optionalUsers.get();
            return UserInfoDto.builder()
                    .name(users.getName())
                    .email(users.getEmail())
                    .picture(users.getPicture())
                    .build();
        }
        return UserInfoDto.builder().build();
    }

    public Users findByEmail(String email) {
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }


}
