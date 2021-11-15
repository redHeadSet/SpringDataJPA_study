package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.Optional;

@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입(){
        // given
        Member member = new Member("test1");
        Member savedMember = memberRepository.save(member);
        Long id = savedMember.getId();

        // when
        Optional<Member> findedOpt = memberRepository.findById(id);
        Member findedMember = findedOpt.get();

        // then
        Assertions.assertThat(savedMember).isEqualTo(findedMember);
    }
}