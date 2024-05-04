package com.xinn.bojcodesandbox;

import cn.hutool.core.io.resource.ResourceUtil;
import com.xinn.bojcodesandbox.model.ExecuteCodeRequest;
import com.xinn.bojcodesandbox.model.ExecuteCodeResponse;

import javax.annotation.Resource;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class JavaNativeCodeSandBoxTest {
    public static void main(String[] args) {
        JavaNativeCodeSandBox javaNativeCodeSandBox = new JavaNativeCodeSandBox();
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setCode(ResourceUtil.readStr("testCode/unsafeCodes/readServerFile/Main.java", StandardCharsets.UTF_8));
        executeCodeRequest.setInputList(Arrays.asList("1 2","1 3"));
        executeCodeRequest.setLanguage("java");
        ExecuteCodeResponse executeCodeResponse = javaNativeCodeSandBox.execute(executeCodeRequest);
        System.out.println(executeCodeResponse);
    }

}