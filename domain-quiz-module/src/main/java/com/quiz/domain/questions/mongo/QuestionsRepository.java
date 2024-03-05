package com.quiz.domain.questions.mongo;

import com.quiz.domain.answers.entity.Answers;
import com.quiz.domain.choice.entity.Choices;
import com.quiz.domain.questions.entity.Questions;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QuestionsRepository extends MongoRepository<Questions, String> {

    @Query("{'_id' :?0}")
    @Update("{'$set' : {'questions' : ?1}}")
    void update(String id, Questions questions);

    @Query("{'id' :?0}")
    @Update("{'$set' : {'choices' : ?1}}")
    void updateChoices(String id, List<Choices> choices);

    @Query("{'quiz_id' :?0}")
    List<Questions> findAllByQuizId(Long quizId);

    @Query("{'_id' :?0}")
    Optional<Questions> findById(String id);

    @Query("{'_id' :?0}")
    @Update("{'$set' : {'answers' :?1}}")
    void updateAnswers(String questionId, Answers newAnswers);

}
