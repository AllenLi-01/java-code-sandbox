package com.xinn.bojcodesandbox;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import com.xinn.bojcodesandbox.model.ExecuteCodeRequest;
import com.xinn.bojcodesandbox.model.ExecuteCodeResponse;
import com.xinn.bojcodesandbox.model.ExecuteMessage;
import com.xinn.bojcodesandbox.model.JudgeInfo;
import com.xinn.bojcodesandbox.security.DefaultSecurityManager;
import com.xinn.bojcodesandbox.utils.ProcessUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class JavaNativeCodeSandBox implements CodeSandBox {
    private static final String CODE_PATH = "tempCode";
    private static final String JAVA_CLASS_NAME = "Main.java";
    private static final long MAX_EXECUTE_TIME = 10000L;
    private static final List<String> BLACK_LIST = Arrays.asList("java.io.File","java.nio"," .net");
    private static final WordTree WORD_TREE;
    static {
        //初始化字典树
        WORD_TREE = new WordTree();
        WORD_TREE.addWords(BLACK_LIST);
    }
    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    @Override
    public ExecuteCodeResponse execute(ExecuteCodeRequest executeCodeRequest) {
        String code = executeCodeRequest.getCode();
        List<String> inputList = executeCodeRequest.getInputList();

        //校验代码是否含敏感命令 从字典树
        FoundWord foundWord = WORD_TREE.matchWord(code);
        if(foundWord != null) {
            return errResponse(new Throwable("代码中包含敏感命令"));
        }


        String userDir = System.getProperty("user.dir");
        String codePath = userDir + File.separator + CODE_PATH;
        //如果代码存放目录不存在，则新建
        if(!FileUtil.exist(codePath)){
            FileUtil.mkdir(codePath);
        }
        //随机生成用户代码存放路径
        String userCodeParentPath = codePath + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + JAVA_CLASS_NAME;
        //生成代码文件
        File userCodeFile = FileUtil.writeString(code,userCodePath, StandardCharsets.UTF_8);

        //编译代码
        String compileCmd = String.format("javac %s",userCodeFile.getAbsolutePath());
        System.out.println(compileCmd);
        try {
            Process compileProcess = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage executeMessage = ProcessUtils.getProcessResult(compileProcess,"编译");
            System.out.println(executeMessage);
        } catch (IOException e) {
            return errResponse(e);
        }

        //执行代码，每个测试用例执行一次，并处理每个测试用例的信息

        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for(String input:inputList){
            String runCmd = String.format("java -Xmx1024m -cp %s Main %s",userCodeParentPath,input);

            try {
                Process process = Runtime.getRuntime().exec(runCmd);
                //使用守护线程确保进程执行时间不超过最大限定值
                new Thread(()->{
                    try {
                        Thread.sleep(MAX_EXECUTE_TIME);
                        process.destroy();

                    }catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
                ExecuteMessage executeMessage = ProcessUtils.getProcessResult(process,input);
                System.out.println(executeMessage);
                executeMessageList.add(executeMessage);
            }catch (Exception e){
                return errResponse(e);
            }
        }
        
        //整理输出结果
        
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        List<String> outputList = new ArrayList<>();
        long maxTime = 0;
        for(ExecuteMessage executeMessage : executeMessageList){
            String errorMessage = executeMessage.getErrorMessage();
            if(StrUtil.isNotBlank(errorMessage)){
                executeCodeResponse.setMessage(errorMessage);
                executeCodeResponse.setStatus(3);
                break;
            }
            outputList.add(executeMessage.getMessage());
            Long time = executeMessage.getTime();
            if(time != null){
                maxTime = Math.max(time,maxTime);
            }
        }
        if(executeCodeResponse.getStatus()==null){
            executeCodeResponse.setStatus(1);
        }
        executeCodeResponse.setOutputList(outputList);
        JudgeInfo judgeInfo = new JudgeInfo();

        //获取程序执行时间
        //todo 每个测试用例单独的执行时间判断
        judgeInfo.setTime(maxTime);
        //todo 统计程序执行内存
//        judgeInfo.setMemory();

        executeCodeResponse.setJudgeInfo(judgeInfo);


        //文件清理
        if(userCodeFile.getParentFile()!=null){
            boolean del = FileUtil.del(userCodeParentPath);
        }

        //错误处理

        return executeCodeResponse;
    }

    /**
     * 某个环节执行异常时，返回错误响应
     * @param e
     * @return
     */
    private ExecuteCodeResponse errResponse(Throwable e){
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        //表示代码沙箱内部错误
        executeCodeResponse.setStatus(2);
        executeCodeResponse.setMessage(e.getMessage());
        return executeCodeResponse;
    }
}
