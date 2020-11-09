package com.zhuanjingkj.zjcbe.facade.service;

import com.zhuanjingkj.zjcbe.data.dto.LoginDTO;
import com.zhuanjingkj.zjcbe.data.dto.ResultDTO;
import com.zhuanjingkj.zjcbe.data.rto.LoginRTO;

public interface IFacadeService {
    public ResultDTO<LoginDTO> login(LoginRTO rto);
}
