package com.example.demo.user.controller.response;

import static com.example.demo.user.domain.UserStatus.ACTIVE;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.user.domain.User;
import org.junit.jupiter.api.Test;

public class UserResponseTest {

	@Test
	public void User으로_응답을_생성할_수_있다() {
		// Given
		User user = User.builder()
		                .id(1L)
		                .email("kok202@naver.com")
		                .nickname("kok202")
		                .address("Seoul")
		                .status(ACTIVE)
		                .lastLoginAt(100L)
		                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
		                .build();

		// When
		UserResponse userResponse = UserResponse.from(user);

		// Then
		assertThat(userResponse.getId()).isEqualTo(1);
		assertThat(userResponse.getEmail()).isEqualTo("kok202@naver.com");
		assertThat(userResponse.getStatus()).isEqualTo(ACTIVE);
		assertThat(userResponse.getLastLoginAt()).isEqualTo(100L);
	}
}
