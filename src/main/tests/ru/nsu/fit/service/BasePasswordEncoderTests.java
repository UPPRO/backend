package ru.nsu.fit.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BasePasswordEncoder.class})
public class BasePasswordEncoderTests {
    private BasePasswordEncoder basePasswordEncoder;

    @Autowired
    public void setBasePasswordEncoder(BasePasswordEncoder basePasswordEncoder) {
        this.basePasswordEncoder = basePasswordEncoder;
    }

    @Test
    public void encode() throws Exception {
        String rawPassword = "Password";
        String encodedPassword = "Password";
        Assert.assertEquals(basePasswordEncoder.encode(rawPassword), encodedPassword);
    }

    @Test
    public void matches() throws Exception {
        String rawPassword = "Password";
        String encodedPassword = "Password";
        Assert.assertTrue(basePasswordEncoder.matches(rawPassword, encodedPassword));
    }
}
