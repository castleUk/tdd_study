package com.example.demo.user.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserServiceTest {

	private UserService userService;

	@BeforeEach
	void init() {
		FakeMailSender fakeMailSender = new FakeMailSender();
		FakeUserRepository fakeUserRepository = new FakeUserRepository();

		this.userService = UserService.builder()
		                              .clockHolder(new TestClockHolder(1678530673958L))
		                              .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
		                              .certificationService(new CertificationService(fakeMailSender))
		                              .userRepository(fakeUserRepository)
		                              .build();

		fakeUserRepository.save(User.builder()
		                            .id(1L)
		                            .email("kok202@naver.com")
		                            .nickname("kok202")
		                            .address("Seoul")
		                            .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
		                            .status(UserStatus.ACTIVE)
		                            .lastLoginAt(0L)
		                            .build());

		fakeUserRepository.save(User.builder()
		                            .id(2L)
		                            .email("kok303@naver.com")
		                            .nickname("kok303")
		                            .address("Seoul")
		                            .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
		                            .status(UserStatus.PENDING)
		                            .lastLoginAt(0L)
		                            .build());
	}
 
	@Test
	void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다() {
		// given
		String email = "kok202@naver.com";

		// when
		User result = userService.getByEmail(email);

		// then
		assertThat(result.getNickname()).isEqualTo("kok202");
	}

	@Test
	void getByEmail은_PENDING_상태인_유저는_찾아올_수_없다() {
		// given
		String email = "kok303@naver.com";

		// when
		// then
		assertThatThrownBy(() -> {
			User result = userService.getByEmail(email);
		}).isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	void getById는_ACTIVE_상태인_유저를_찾아올_수_있다() {
		// given
		// when
		User result = userService.getById(1);

		// then
		assertThat(result.getNickname()).isEqualTo("kok202");
	}

	@Test
	void getById는_PENDING_상태인_유저는_찾아올_수_없다() {
		// given
		// when
		// then
		assertThatThrownBy(() -> {
			User result = userService.getById(2);
		}).isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	void userCreate_를_이용하여_유저를_생성할_수_있다() {
		// given
		UserCreate userCreate = UserCreate.builder()
		                                  .email("kok202@kakao.com")
		                                  .address("Gyeongi")
		                                  .nickname("kok202-k")
		                                  .build();

		// when
		User result = userService.create(userCreate);

		// then
		assertThat(result.getId()).isNotNull();
		assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
		assertThat(result.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
	}

	@Test
	void userUpdateDto_를_이용하여_유저를_수정할_수_있다() {
		// given
		UserUpdate userUpdate = UserUpdate.builder()
		                                  .address("Incheon")
		                                  .nickname("kok202-n")
		                                  .build();

		// when
		userService.update(1, userUpdate);

		// then
		User user = userService.getById(1);
		assertThat(user.getId()).isNotNull();
		assertThat(user.getAddress()).isEqualTo("Incheon");
		assertThat(user.getNickname()).isEqualTo("kok202-n");
	}

	@Test
	void user를_로그인_시키면_마지막_로그인_시간이_변경된다() {
		// given
		// when
		userService.login(1);

		// then
		User user = userService.getById(1);
		assertThat(user.getLastLoginAt()).isGreaterThan(0L);
		assertThat(user.getLastLoginAt()).isEqualTo(1678530673958L);
	}

	@Test
	void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다() {
		// given
		// when
		userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");

		// then
		User user = userService.getById(2);
		assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
	}

	@Test
	void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다() {
		// given
		// when
		// then
		assertThatThrownBy(() -> {
			userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaac");
		}).isInstanceOf(CertificationCodeNotMatchedException.class);
	}

}
