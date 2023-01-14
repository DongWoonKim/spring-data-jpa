package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

    @Test
    public void testMember() {
        System.out.println("memberRepository = " + memberRepository.getClass());
        Member member = Member.builder1()
                .username("memberA")
                .build();
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertEquals(savedMember.getId(), findMember.getId());
        assertEquals(savedMember.getUsername(), findMember.getUsername());
        assertEquals(savedMember, findMember);
    }

    @Test
    public void basicCRUD() {
        Member member1 = Member.builder1().username("member1").build();
        Member member2 = Member.builder1().username("member2").build();
        memberRepository.save( member1 );
        memberRepository.save( member2 );

        // 단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertEquals(findMember1, member1);
        assertEquals(findMember2, member2);

        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertEquals(all.size(), 2);
        long count = memberRepository.count();
        assertEquals(count, 2);

        // 삭제
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long delCount = memberRepository.count();
        assertEquals(delCount, 0);

    }

    @Test
    public void findByUsernameAndAgeGreaterThen() {

        Member m1 = new Member("memberA", 10);
        Member m2 = new Member("memberB", 20);
        Member savedM1 = memberRepository.save(m1);
        Member savedM2 = memberRepository.save(m2);

        List<Member> m1Result = memberRepository.findByUsernameAndAgeGreaterThan(savedM1.getUsername(), 8);
        List<Member> m2Result = memberRepository.findByUsernameAndAgeGreaterThan(savedM2.getUsername(), 8);

        assertEquals(m1Result.get(0).getUsername(), savedM1.getUsername());
        assertEquals(m2Result.get(0).getUsername(), savedM2.getUsername());

    }

    @Test
    public void findHelloBy() {
        List<Member> helloBy = memberRepository.findTop3HelloBy();
    }

    @Test
    public void testNamedQuery() {

        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save( m1 );
        memberRepository.save( m2 );

        List<Member> result = memberRepository.findByUsername("AAA");
        Member findMember = result.get(0);

        assertEquals(findMember, m1);
    }

    @Test
    public void testQuery() {

        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        assertEquals(result.get(0), m1);

    }

    @Test
    public void findUsernameList() {

        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s = " + s);
        }

    }

    @Test
    public void findMemberDto() {

        Member m1 = new Member("AAA", 10);
        memberRepository.save (m1);

        Team team = new Team("teamA");
        m1.setTeam( team );
        teamRepository.save(team);

        List<MemberDto> memberDtoList = memberRepository.findMemberDto();
        for (MemberDto dto : memberDtoList) {
            System.out.println("dto = " + dto);
        }

    }

    @Test
    public void findByNames() {

        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member member : result) {
            System.out.println("member = " + member);
        }

    }

    @Test
    public void returnType() {

        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> aaa1 = memberRepository.findListByUsername("AAA");
        Member aaa2 = memberRepository.findMemberByUsername("AAA");
        Optional<Member> aaa3 = memberRepository.findOptionalByUsername("AAAB");

        System.out.println(aaa1);
        System.out.println(aaa2);
        System.out.println(aaa3);

    }


}