package org.webp.intro.spring.bean.profile;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.webp.intro.spring.bean.jpa.Application;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class H2Test extends DbTestBase {


    @Override
    protected String getExpectedDatabaseName() {
        return "h2";
    }
}
