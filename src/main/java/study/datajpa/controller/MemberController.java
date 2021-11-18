package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @PostConstruct
    public void init() {
        memberRepository.save(new Member("mem1", 10));
        memberRepository.save(new Member("mem2", 20));
        memberRepository.save(new Member("mem3", 30));
        memberRepository.save(new Member("mem4", 40));
        memberRepository.save(new Member("mem5", 50));
        memberRepository.save(new Member("mem6", 60));
    }

    // 기존 id를 받아서 조회하는 방식
    @GetMapping("/members/{id}")
    public String findById(@PathVariable("id") Long id) {
        Member findedMem = memberRepository.findById(id).get();
        return findedMem.getUsername();
    }

    // 도메인 컨버터 방식 - 함수 인자를 Member 객체로 받아 처리
    // id가 member의 pk인 것을 아는 상황에서 처리 가능
    // 하지만, 운영 시 pk 값이 get 방식으로 이동하는 경우는 거의 없으므로 사실상 권장하지 않음
    // + 조회인 경우에는 사용이 어느정도 가능하나, update 등 처리 시 Transaction 범위를 알 수 없음
    @GetMapping("/members2/{id}")
    public String findById2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    // web에서도 Pageable 지원
    // localhost:8080/members?page=0 으로 입력하면 기본적으로 size 값이 20으로 들어감
    // page=0
    // size=3
    // sort=id,desc (여러 번 입력도 가능)
    // global 설정은 application.yml 파일에서 설정 가능
    @GetMapping("/members")
    public Page<Member> list(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    // 각 조회 상태에 따라 @PageableDefault 값에서 개별 처리 가능
    @GetMapping("/members/custom")
    public Page<Member> listCustom(@PageableDefault(size = 5, sort = "username") Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    // Page 처리 추가 고려 사항
    // 1. DTO를 사용하여 처리할 것 - page.map 처리
    // 2. page index는 0부터 처리하는 걸로 하는 걸로 처리한다 - page index 화면에서는 +1해서 보여주는 걸로
}
