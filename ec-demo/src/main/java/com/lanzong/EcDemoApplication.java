package com.lanzong;


import com.lanzong.dao.UserDOMapper;
import com.lanzong.dataobject.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class EcDemoApplication {

    @Autowired
    private UserDOMapper userDOMapper;

    @RequestMapping("/users")
    public String home(){
        UserDO userDO = userDOMapper.selectByPrimaryKey(1);
        return userDO.getName();
    }

    public static void main(String[] args) {
        SpringApplication.run(EcDemoApplication.class, args);
    }

}
