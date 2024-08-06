package com.example.demo.post.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

public class PostTest {

	@Test
	public void PostCreate으로_게시물을_만들_수_있다() {
		// Given
		PostCreate postCreate = PostCreate.builder()
		                                  .writerId(1)
		                                  .content("helloworld")
		                                  .build();

		User writer = User.builder()
		                  .id(1L)
		                  .email("kok202@naver.com")
		                  .nickname("kok202")
		                  .address("Seoul")
		                  .status(UserStatus.ACTIVE)
		                  .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
		                  .build();

		// When
		Post post = Post.from(writer, postCreate, new TestClockHolder(1678530673958L));

		// Then
		assertThat(post.getContent()).isEqualTo("helloworld");
		assertThat(post.getCreatedAt()).isEqualTo(1678530673958L);
		assertThat(post.getWriter()
		               .getEmail()).isEqualTo("kok202@naver.com");
		assertThat(post.getWriter()
		               .getNickname()).isEqualTo("kok202");
		assertThat(post.getWriter()
		               .getAddress()).isEqualTo("Seoul");
		assertThat(post.getWriter()
		               .getStatus()).isEqualTo(UserStatus.ACTIVE);
		assertThat(post.getWriter()
		               .getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");

	}

	@Test
	public void PostUpdate로_게시물을_수정할_수_있다() {
		// Given
		PostUpdate postUpdate = PostUpdate.builder()
		                                  .content("foobar")
		                                  .build();

		User writer = User.builder()
		                  .id(1L)
		                  .email("kok202@naver.com")
		                  .nickname("kok202")
		                  .address("Seoul")
		                  .status(UserStatus.ACTIVE)
		                  .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
		                  .build();

		Post post = Post.builder()
		                .id(1L)
		                .content("helloworld")
		                .createdAt(1678530673958L)
		                .modifiedAt(0L)
		                .writer(writer)
		                .build();

		// When
		post = post.update(postUpdate, new TestClockHolder(1678530673958L));

		// Then
		assertThat(post.getContent()).isEqualTo("foobar");
		assertThat(post.getModifiedAt()).isEqualTo(1678530673958L);


	}
}
