package com.xinn.bojcodesandbox.controller;

import com.xinn.bojcodesandbox.JavaDockerCodeSandbox;
import com.xinn.bojcodesandbox.model.ExecuteCodeRequest;
import com.xinn.bojcodesandbox.model.ExecuteCodeResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("/")
public class ExecController {
    @Resource
    JavaDockerCodeSandbox javaDockerCodeSandbox;

    @PostMapping("/exec")
    ExecuteCodeResponse exec(@RequestBody ExecuteCodeRequest executeCodeRequest) {
        return javaDockerCodeSandbox.execute(executeCodeRequest);
    }
}
