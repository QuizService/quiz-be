package com.quiz.lock;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class CustomKeyParserTest {
    @Test
    void testCustomKeyParser() {
        Long quizId = 1L;
        String key = "'save user first quizId - ' + #quizId";
        String[] parametersNames = new String[]{"quizId"};
        Object[] args = new Object[]{1L};
        Object keyName = CustomKeyParser.getKeyNameSuffix(parametersNames, args, key);

        String result = (String) keyName;

        System.out.println(result);
    }
}
