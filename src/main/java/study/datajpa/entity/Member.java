package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})   // toString 출력 제한
@NamedQuery(
        name = "Member.namedQueryTest",
        query = "select m from Member m" +
                " where m.username = :username"
        // [강점] : 네임드쿼리는 정적 쿼리라서 해당 문법에 에러가 있다면,
        // 어플리케이션 로딩 시점에 빌드 에러가 발생한다
)
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    // 프록시 구현체가 생성 시 사용해야 함 - NoArgsConstructor 처리
//    protected Member(){}

    public Member(String username) {
        this.username = username;
        this.age = 0;
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public void changeTeam(Team newTeam) {
        this.team = newTeam;
        team.getMembers().add(this);
    }
}
