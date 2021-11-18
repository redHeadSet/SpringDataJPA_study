package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @PostConstruct
    public void init() {
        memberRepository.save(new Member("test1"));
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
}
