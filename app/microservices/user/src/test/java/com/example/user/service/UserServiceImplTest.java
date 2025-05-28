package com.example.user.service;


import com.example.user.dto.UserRequestDTO;
import com.example.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import lombok.AllArgsConstructor;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@AllArgsConstructor
@SpringBootTest
public class UserServiceImplTest {
    @Autowired
    private UserServiceImpl userServiceImpl;

    @Test
    public void CreateTest(){
        UserRequestDTO userRequestDTO = new UserRequestDTO("jax2", "john.hs2@gmail.com");
        System.out.println(userServiceImpl.create(userRequestDTO));

    }



}
