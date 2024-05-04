package com.xinn.bojcodesandbox;

import com.xinn.bojcodesandbox.model.ExecuteCodeRequest;
import com.xinn.bojcodesandbox.model.ExecuteCodeResponse;

/**
 * 代码沙箱接口
 */
public interface CodeSandBox {
    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse execute(ExecuteCodeRequest executeCodeRequest);
}
