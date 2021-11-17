package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;
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


    @Test
    public void test2(){
        // given
        Member member1 = new Member("same name", 20);
        Member member2 = new Member("same name", 30);
        Member member3 = new Member("same name", 40);
        Member member4 = new Member("same name", 50);
        Member member5 = new Member("same name", 60);
        Member member6 = new Member("same name", 70);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);
        memberRepository.save(member6);

        // when
        List<Member> ungt = memberRepository.findByUsernameAndAgeGreaterThan("same name", 40);

        // then
        ungt.forEach(member -> {
            System.out.println("age : " + member.getAge());
            Assertions.assertThat(member.getAge()).isGreaterThan(40);
        });
    }
}