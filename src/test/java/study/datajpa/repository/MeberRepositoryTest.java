package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class MeberRepositoryTest {

    @Autowired MemberRepository memberRepository;

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

}