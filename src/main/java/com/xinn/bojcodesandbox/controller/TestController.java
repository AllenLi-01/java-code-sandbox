//package com.xinn.bojcodesandbox.controller;
//
//import cn.hutool.core.io.resource.ResourceUtil;
//import com.github.dockerjava.api.DockerClient;
//import com.github.dockerjava.api.command.PullImageCmd;
//import com.github.dockerjava.api.command.PullImageResultCallback;
//import com.github.dockerjava.api.model.PullResponseItem;
//import com.github.dockerjava.core.DockerClientBuilder;
//import com.xinn.bojcodesandbox.JavaDockerCodeSandbox;
//import com.xinn.bojcodesandbox.model.ExecuteCodeRequest;
//import com.xinn.bojcodesandbox.model.ExecuteCodeResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.nio.charset.StandardCharsets;
//import java.util.Arrays;
//
//@Slf4j
//@RestController("/")
//public class TestController {
//    @GetMapping("/test")
//    public void test(){
//        JavaDockerCodeSandbox javaDockerCodeSandbox = new JavaDockerCodeSandbox();
//        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
//        executeCodeRequest.setInputList(Arrays.asList("1 2", "1 3","4 5","10000 20000","10000 2023000","100001214 20021500","10066600 20088800"));
////        String code = ResourceUtil.readStr("testCode/simpleComputeArgs/Main.java.java", StandardCharsets.UTF_8);
//        String code = ResourceUtil.readStr("testCode/acmIOCodes/Main.java", StandardCharsets.UTF_8);
////        String code = ResourceUtil.readStr("testCode/simpleCompute/Main.java.java", StandardCharsets.UTF_8);
//        executeCodeRequest.setCode(code);
//        executeCodeRequest.setLanguage("java");
//        ExecuteCodeResponse executeCodeResponse = javaDockerCodeSandbox.execute(executeCodeRequest);
//        log.info(String.valueOf(executeCodeResponse));
//    }
//}
