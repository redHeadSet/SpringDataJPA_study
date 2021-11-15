package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {
    @Autowired
    MemberJpaRepository memberJpaRepository;
    
    @Test // jupiter api 사용할 것
    public void 회원가입(){
        // given
        Member member = new Member("test1");
        Long savedId = memberJpaRepository.save(member);

        // when
        Member findMember = memberJpaRepository.findOne(savedId);

        // then
        Assertions.assertThat(member).isEqualTo(findMember);
    }
}