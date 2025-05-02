package com.example.User.service;


import com.example.User.dto.UserRequestDTO;
import com.example.User.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@SpringBootTest
public class UserServiceImplTest {
    @Autowired
    private UserServiceImpl userServiceImpl;

    @Test
    public void CreateTest(){
        UserRequestDTO userRequestDTO = new UserRequestDTO("John2", "john.hs2@gmail.com");
        System.out.println(userServiceImpl.create(userRequestDTO));

    }

}
