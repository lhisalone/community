package life.majiang.community.community;

import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.model.Question;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CommunityApplicationTests {

    @Autowired
    QuestionMapper questionMapper;

    @Test
    void contextLoads() {
        Question question = new Question();
        question.setDescription("123");
        question.setGmtModified(111L);
        question.setCommentCount(1);
        question.setCreator(12);
        question.setId(123123);
        question.setLikeCount(1111);
        question.setTitle("ahhahaha");
        question.setViewCount(222);
        question.setTag("wowowowo");
        question.setGmtCreate(222L);
        questionMapper.create(question);
        System.out.println("ok");

    }

}
