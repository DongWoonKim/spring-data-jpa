package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() {
        Member member = Member.builder1()
                .username("memberA")
                .build();
        Member savedMember = memberJpaRepository.save(member);
        Member findMember = memberJpaRepository.find(savedMember.getId());

        assertEquals(savedMember.getId(), findMember.getId());
        assertEquals(savedMember.getUsername(), findMember.getUsername());
        assertEquals(savedMember, findMember);
    }

    @Test
    public void basicCRUD() {
        Member member1 = Member.builder1().username("member1").build();
        Member member2 = Member.builder1().username("member2").build();
        memberJpaRepository.save( member1 );
        memberJpaRepository.save( member2 );

        // 단건 조회 검증
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();
        assertEquals(findMember1, member1);
        assertEquals(findMember2, member2);

        // 리스트 조회 검증
        List<Member> all = memberJpaRepository.findAll();
        assertEquals(all.size(), 2);
        long count = memberJpaRepository.count();
        assertEquals(count, 2);

        // 삭제
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);
        long delCount = memberJpaRepository.count();
        assertEquals(delCount, 0);

    }

    @Test
    public void findByUsernameAndAgeGreaterThen() {

        Member m1 = new Member("memberA", 10);
        Member m2 = new Member("memberB", 20);
        Member savedM1 = memberJpaRepository.save(m1);
        Member savedM2 = memberJpaRepository.save(m2);

        List<Member> m1Result = memberJpaRepository.findByUsernameAndAgeGreaterThen(savedM1.getUsername(), 8);
        List<Member> m2Result = memberJpaRepository.findByUsernameAndAgeGreaterThen(savedM2.getUsername(), 8);

        assertEquals(m1Result.get(0).getUsername(), savedM1.getUsername());
        assertEquals(m2Result.get(0).getUsername(), savedM2.getUsername());

    }

    @Test
    public void Paging() {

        // given
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 10));
        memberJpaRepository.save(new Member("member3", 10));
        memberJpaRepository.save(new Member("member4", 10));
        memberJpaRepository.save(new Member("member5", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        // when
        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        // then
        assertEquals(members.size(), limit);
        assertEquals(totalCount, 5);

    }

}