package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Fail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {
    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test // jupiter api 사용할 것
    public void test1() {
        // given
        Member member = new Member("test1");
        Long savedId = memberJpaRepository.save(member);

        // when
        Member findMember = memberJpaRepository.findOne(savedId);

        // then
        Assertions.assertThat(member).isEqualTo(findMember);
    }

    @Test
    public void test2() {
        // given
        Member member1 = new Member("mem1", 10);
        Member member2 = new Member("mem2", 20);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        // when
        List<Member> all = memberJpaRepository.findAll();
        Long count = memberJpaRepository.count();

        // then
        Assertions.assertThat(count).isEqualTo(2);

        all.forEach(each -> {
            if((member1.getId() != each.getId()) && (member2.getId() != each.getId()))
                Assertions.fail("id Fail");
            else
                System.out.println("each run : " + each.getUsername());
        });


        // given

        // when
        memberJpaRepository.delete(member1);

        // then
        List<Member> all2 = memberJpaRepository.findAll();
        Long count2 = memberJpaRepository.count();

        Assertions.assertThat(1L).isEqualTo(count2);
        all2.forEach(each -> {
            if(each.getId() != member2.getId())
                Assertions.fail("delete Fail");
            else
                System.out.println("member2 : " + member2.getUsername());
        });
    }
}