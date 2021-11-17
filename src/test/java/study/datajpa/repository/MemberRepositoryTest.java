package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;

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
        makeSamename();

        // when
        List<Member> ungt = memberRepository.findByUsernameAndAgeGreaterThan("same name", 40);

        // then
        ungt.forEach(member -> {
            System.out.println("age : " + member.getAge());
            Assertions.assertThat(member.getAge()).isGreaterThan(40);
        });
    }

    @Test
    public void nameQueryTest() {
        // given
        Member member1 = new Member("findName", 20);
        memberRepository.save(member1);

        // when
        List<Member> findMember = memberRepository.tryNamedQuery("findName");

        // then
        Assertions.assertThat(findMember.size()).isEqualTo(1);
        findMember.forEach(each -> {
            Assertions.assertThat(member1.getId()).isEqualTo(each.getId());
        });
    }

    @Test
    public void RepositoryQueryTest(){
        // given
        makeSamename();

        // when
        List<Member> rmq = memberRepository.repositoryMethodQuery("same name", 40);
        List<String> userNameList = memberRepository.findByUserNameList();

        // then
        rmq.forEach(each -> {
            System.out.println("age : " + each.getAge());
            Assertions.assertThat(each.getAge()).isGreaterThan(40);
        });
        userNameList.forEach(username -> {
            System.out.println("username : " + username);
        });
    }

    @Test
    public void DtoTest(){
        // given
        makeDefault();

        // when
        List<MemberDto> dtos = memberRepository.findDtos();

        // then
        dtos.forEach(each -> {
            System.out.println("id : " + each.getId()
                            + " name : " + each.getUsername()
                            + " team : " + each.getTeamname());
        });
    }

    @Test
    public void multiReturnTest() {
        // given
        makeDefault();

        // when
        List<Member> listMem = memberRepository.findMemberListByUsername("mem1");
        Member singleMem = memberRepository.findSingleMemberByUsername("mem1");
        Optional<Member> optMem = memberRepository.findOptionalMemberByUsername("mem1");

        // then
        Assertions.assertThat(listMem.size()).isEqualTo(1);
        Assertions.assertThat(singleMem).isEqualTo(listMem.get(0));
        Assertions.assertThat(singleMem).isEqualTo(optMem.get());
    }

    private void makeSamename() {
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
    }

    private void makeDefault() {
        Member member1 = new Member("mem1", 20);
        Member member2 = new Member("mem2", 30);
        Member member3 = new Member("mem3", 40);
        Member member4 = new Member("mem4", 50);
        Member member5 = new Member("mem5", 60);
        Member member6 = new Member("mem6", 70);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);
        memberRepository.save(member6);

        Team team1 = new Team("team1");
        Team team2 = new Team("team2");
        teamRepository.save(team1);
        teamRepository.save(team2);

        member1.changeTeam(team1);
        member2.changeTeam(team1);
        member3.changeTeam(team1);
        member4.changeTeam(team2);
        member5.changeTeam(team2);
        member6.changeTeam(team2);
    }
}