package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

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
        makeSamename("same name");

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
        makeSamename("same name");

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

    @Test
    public void Pageing() {
        // given
        int age = 30;
        makeSameAge(age);
        int offset = 0, limit = 3;

        // when
        // 중요 : page index 는 0부터 시작한다!!
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        Page<Member> page = memberRepository.findPageByAge(age, pageRequest);

        // then
        List<Member> content = page.getContent();
        content.forEach(member -> {
            System.out.println("page member - " + member.getUsername() + "(" + member.getAge() + ")");
        });

        System.out.println("page getTotalElements : "+ page.getTotalElements());
        System.out.println("page getNumber : "+ page.getNumber());
        System.out.println("page getTotalPages : "+ page.getTotalPages());
        System.out.println("page isFirst : "+ page.isFirst());
        System.out.println("page hasNext : "+ page.hasNext());

        // DTO 처리 예시
        Page<MemberDto> exportDto = page.map(member -> new MemberDto(member.getId(), member.getUsername(), "team name"));

        // then ~ 2
        Slice<Member> slice = memberRepository.findSliceByAge(age, pageRequest);
        // -> 실제 쿼리는 limit 4로 나간다 (size가 3인데도 불구하고)
        List<Member> slice_content = slice.getContent();
        // 하지만 Content는 3개로 출력됨
        content.forEach(member -> {
            System.out.println("slice member - " + member.getUsername() + "(" + member.getAge() + ")");
        });

        System.out.println("slice getNumber : "+ slice.getNumber());
        System.out.println("slice isFirst : "+ slice.isFirst());
        System.out.println("slice hasNext : "+ slice.hasNext());
    }

    @Test
    public void bulkTest() {
        // given
        makeDefault();

        // when
        int updatedRow = memberRepository.agePlus(30);
//        em.flush(); em.clear(); // clearAutomatically 값으로 처리

        // then
        Assertions.assertThat(updatedRow).isEqualTo(5);
    }

    @Test
    public void findMemberLazy() {
        // given
        makeDefault();
        em.flush(); em.clear();

        // when
//        List<Member> all = memberRepository.findAll();
        List<Member> all = memberRepository.findAllFetchJoin();

        // then
        all.forEach(member -> {
            System.out.println("member.getUsername() = " + member.getUsername());
        });
    }

    @Test
    public void queryHint(){
        // given
        Member hi = memberRepository.save(new Member("hi", 10));
        em.flush(); em.clear();

        // when
        Member findedMember = memberRepository.findReadOnlyByUsername("hi");
//        findedMember.setUsername("hi2");


        // then
    }

    @Test
    public void baseEntityTest() throws InterruptedException {
        // given
        Member base = memberRepository.save(new Member("base"));
        System.out.println("name = " + base.getUsername());
        System.out.println("createDate = " + base.getCreatedDate());
//        System.out.println("updateDate = " + base.getUpdatedDate());

        Thread.sleep(1000);
        base.setUsername("new name");
        em.flush();

        System.out.println("new name = " + base.getUsername());
        System.out.println("new createDate = " + base.getCreatedDate());
//        System.out.println("new updateDate = " + base.getUpdatedDate());
    }

    // QueryByExample
    // Outer Join 안됨, 단순 비교 정도만 가능
    // QueryDSL 사용으로 해결 가능
    @Test
    public void queryExam() {
        // given
        makeDefault();
        em.flush();
        em.clear();

        // when
        Member member = new Member("mem1");
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("age");

//        Example<Member> memberExample = Example.of(member);
        Example<Member> memberExample = Example.of(member, matcher);

        List<Member> findMemList = memberRepository.findAll(memberExample);
        List<Member> all = memberRepository.findAll();
        System.out.println("all size : " + all.size());

        // then
        if (findMemList.size() != 1)
            Assertions.fail("size not 1 [" + findMemList.size() + "]");
        System.out.println("findMemList = " + findMemList.get(0).getUsername());
    }

    @Test
    public void projections(){
        // given
        makeDefault();

        // when
        // 인터페이스만 만들면 구현체를 Data JPA가 만들어서 처리 가능
        List<GetUserNameOnly> projectionsResult = memberRepository.findProjectionsByUsername("mem1");

        // then
        for (GetUserNameOnly userNameOnly : projectionsResult) {
            System.out.println("userNameOnly = " + userNameOnly);
        }
    }

    @Test
    public void nativeTest() {
        // given
        makeDefault();

        // when
        Member mem1 = memberRepository.findMemberNative("mem1");

        // then
        System.out.println("mem1 = " + mem1.getUsername());
    }

    private void makeSamename(String name) {
        Member member1 = new Member(name, 20);
        Member member2 = new Member(name, 30);
        Member member3 = new Member(name, 40);
        Member member4 = new Member(name, 50);
        Member member5 = new Member(name, 60);
        Member member6 = new Member(name, 70);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);
        memberRepository.save(member6);
    }

    private void makeSameAge(int age) {
        Member member1 = new Member("mem1", age);
        Member member2 = new Member("mem2", age);
        Member member3 = new Member("mem3", age);
        Member member4 = new Member("mem4", age);
        Member member5 = new Member("mem5", age);
        Member member6 = new Member("mem6", age);
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