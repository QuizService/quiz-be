package com.quiz.global.sequence;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "questions_sequences")
public class DBSequence {

    @Id
    private String id;

    private Long seq;


}
