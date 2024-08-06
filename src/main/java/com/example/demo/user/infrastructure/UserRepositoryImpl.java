package com.example.demo.user.infrastructure;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.service.port.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

	private final UserJpaRepository userJpaRepository;

	@Override
	public Optional<User> findById(long id) {
		return userJpaRepository.findById(id)
		                        .map(UserEntity::toModel);
	}

	@Override
	public User getById(long id) {
		return findById(id).orElseThrow(() -> new ResourceNotFoundException("Users", id));
	}

	@Override
	public Optional<User> findByIdAndStatus(long id, UserStatus userStatus) {
		return userJpaRepository.findByIdAndStatus(id, userStatus)
		                        .map(UserEntity::toModel);
	}

	@Override
	public Optional<User> findByEmailAndStatus(String email, UserStatus userStatus) {
		return userJpaRepository.findByEmailAndStatus(email, userStatus)
		                        .map(UserEntity::toModel);
	}

	@Override
	public User save(User user) {
		return userJpaRepository.save(UserEntity.fromModel(user))
		                        .toModel();
		// user.toEntity() 식으로 작성하는 방법도 있지만, 도메인은 infra레이어의 정보를 모르는것이 좋다.
	}


}
