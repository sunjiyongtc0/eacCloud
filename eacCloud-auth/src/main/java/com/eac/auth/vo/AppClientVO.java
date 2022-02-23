package com.eac.auth.vo;


import com.eac.common.core.vo.CommonVO;
import java.io.Serializable;

/**
 * <pre>
 * Title : 应用信息的参数类
 * </pre>
 *
 * @author xiashilong
 * @since 2021-11-18
 **/
public class AppClientVO extends CommonVO implements Serializable {

    private static final long serialVersionUID = -7365233858896287067L;
    private String clientCode;// 应用编码
    private String authType;// 授权类型

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

}
