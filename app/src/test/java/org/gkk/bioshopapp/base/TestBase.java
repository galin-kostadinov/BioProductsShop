package org.gkk.bioshopapp.base;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TestBase {

    @BeforeEach
    public void setupTest(){
        MockitoAnnotations.initMocks(this);
    }
}
