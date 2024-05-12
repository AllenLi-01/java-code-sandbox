package com.xinn.bojcodesandbox.utils;


import cn.hutool.core.util.StrUtil;
import com.xinn.bojcodesandbox.model.ExecuteMessage;
import org.springframework.util.StopWatch;

import java.io.*;

public class ProcessUtils {
    public static ExecuteMessage getProcessResult(Process process, String opName) {
        ExecuteMessage message = new ExecuteMessage();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            int exitValue = process.waitFor();
            message.setExitValue(exitValue);
            if (exitValue == 0) {//正常输出
                System.out.println(opName+"成功");
                //逐行读取正常输出
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine())!=null){
                    output.append(line).append("\n");
                }
                message.setMessage(output.toString());
            }else {//异常输出
                System.out.println(opName+"失败，错误码："+exitValue);
                //逐行读取正常输出
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine())!=null){
                    output.append(line).append("\n");
                }
                message.setMessage(output.toString());

                //逐行读取异常输出
                BufferedReader errorBufferReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                StringBuilder errorOutput = new StringBuilder();
                String errLine;
                while ((errLine = errorBufferReader.readLine())!=null){
                    errorOutput.append(errLine);
                }

                message.setErrorMessage(errorOutput.toString());
                System.out.println("错误输出："+message.getErrorMessage());
            }

        } catch (InterruptedException | IOException e) {

            System.err.println(e.getMessage());
        } finally {
            stopWatch.stop();
            message.setTime(stopWatch.getLastTaskTimeMillis());
        }
        return message;
    }

    public static ExecuteMessage runInteractProcessAndGetMessage(Process runProcess, String args) {
        ExecuteMessage executeMessage = new ExecuteMessage();

        try {
            // 向控制台输入程序
            OutputStream outputStream = runProcess.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            String[] s = args.split(" ");
            String join = StrUtil.join("\n", s) + "\n";
            outputStreamWriter.write(join);
            // 相当于按了回车，执行输入的发送
            outputStreamWriter.flush();

            // 分批获取进程的正常输出
            InputStream inputStream = runProcess.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder compileOutputStringBuilder = new StringBuilder();
            // 逐行读取
            String compileOutputLine;
            while ((compileOutputLine = bufferedReader.readLine()) != null) {
                compileOutputStringBuilder.append(compileOutputLine);
            }
            executeMessage.setMessage(compileOutputStringBuilder.toString());
            // 记得资源的释放，否则会卡死
            outputStreamWriter.close();
            outputStream.close();
            inputStream.close();
            runProcess.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return executeMessage;
    }

}
