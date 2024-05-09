package com.quiz.global.sequence;

import com.quiz.global.sequence.DBSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
@RequiredArgsConstructor
public class SequenceGenerator {
    private final MongoOperations mongoOperations;

    public Long generateSequence(String seqName) {
        DBSequence counter = mongoOperations
                .findAndModify(Query.query(where("_id")
                        .is(seqName)),
                        new Update().inc("seq",1), FindAndModifyOptions.options().returnNew(true).upsert(true),
                        DBSequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }
}
