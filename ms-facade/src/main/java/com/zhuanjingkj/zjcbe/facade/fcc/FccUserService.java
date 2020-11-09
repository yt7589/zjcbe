package com.zhuanjingkj.zjcbe.facade.fcc;

import com.zhuanjingkj.zjcbe.data.dto.LoginDTO;
import com.zhuanjingkj.zjcbe.data.dto.ResultDTO;
import com.zhuanjingkj.zjcbe.data.rto.LoginRTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value="ms-user", path="/user")
public interface FccUserService {
    @PostMapping("/login")
    public ResultDTO<LoginDTO> login(@RequestBody LoginRTO rto);
}
