package com.example.demo.user.domain;

import static com.example.demo.user.domain.UserStatus.ACTIVE;
import static com.example.demo.user.domain.UserStatus.PENDING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import org.junit.jupiter.api.Test;

public class UserTest {

	@Test
	void UserCreate_객체로_생성할_수_있다() {
		// Given
		UserCreate userCreate = UserCreate.builder()
		                                  .email("kok202@kakao.com")
		                                  .address("kok202")
		                                  .nickname("Pangyo")
		                                  .build();

		// When
		User user = User.from(userCreate, new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"));

		// Then
		assertThat(user.getId()).isNull();
		assertThat(user.getEmail()).isEqualTo("kok202@kakao.com");
		assertThat(user.getAddress()).isEqualTo("kok202");
		assertThat(user.getNickname()).isEqualTo("Pangyo");
		assertThat(user.getStatus()).isEqualTo(PENDING);
		assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
	}


	@Test
	void UserUpdate_객체로_데이터를_업데이트_할_수_있다() {
		// Given
		User user = User.builder()
		                .id(1L)
		                .email("kok202@naver.com")
		                .nickname("kok202")
		                .address("Seoul")
		                .status(UserStatus.ACTIVE)
		                .lastLoginAt(100L)
		                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
		                .build();

		UserUpdate userUpdate = UserUpdate.builder()
		                                  .nickname("kok202-k")
		                                  .address("Pangyo")
		                                  .build();

		// When
		user = user.update(userUpdate);

		// Then
		assertThat(user.getId()).isEqualTo(1L);
		assertThat(user.getEmail()).isEqualTo("kok202@naver.com");
		assertThat(user.getNickname()).isEqualTo("kok202-k");
		assertThat(user.getAddress()).isEqualTo("Pangyo");
		assertThat(user.getStatus()).isEqualTo(ACTIVE);
		assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");
		assertThat(user.getLastLoginAt()).isEqualTo(100L);
	}

	@Test
	void 로그인을_할_수_있고_로그인시_마지막_로그인_시간이_변경된다() {
		// Given
		User user = User.builder()
		                .id(1L)
		                .email("kok202@naver.com")
		                .nickname("kok202")
		                .address("Seoul")
		                .status(UserStatus.ACTIVE)
		                .lastLoginAt(100L)
		                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
		                .build();

		// When
		user = user.login(new TestClockHolder(1678530673958L));

		// Then
		assertThat(user.getLastLoginAt()).isEqualTo(1678530673958L);
	}

	@Test
	void 유효한_인증_코드로_계정을_활성화_할_수_있다() {
		// Given
		User user = User.builder()
		                .id(1L)
		                .email("kok202@naver.com")
		                .nickname("kok202")
		                .address("Seoul")
		                .status(PENDING)
		                .lastLoginAt(100L)
		                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
		                .build();

		// When
		user = user.certificate("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");

		// Then
		assertThat(user.getStatus()).isEqualTo(ACTIVE);
	}

	@Test
	void 잘못된_인증_코드로_계정을_활성화_하려하면_에러를_던진다() {
		// Given
		User user = User.builder()
		                .id(1L)
		                .email("kok202@naver.com")
		                .nickname("kok202")
		                .address("Seoul")
		                .status(PENDING)
		                .lastLoginAt(100L)
		                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
		                .build();

		// When
		// Then
		assertThatThrownBy(() -> {
			user.certificate("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
		}).isInstanceOf(CertificationCodeNotMatchedException.class);
	}


}
