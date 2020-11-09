package com.zhuanjingkj.zjcbe.facade.controller;

import com.zhuanjingkj.zjcbe.data.dto.LoginDTO;
import com.zhuanjingkj.zjcbe.data.dto.ResultDTO;
import com.zhuanjingkj.zjcbe.data.rto.LoginRTO;
import com.zhuanjingkj.zjcbe.facade.service.FacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/facade")
public class FacadeController {
    @Autowired
    FacadeService facadeService;

    @PostMapping("/login")
    public ResultDTO<LoginDTO> login(@RequestBody LoginRTO rto) {
        return facadeService.login(rto);
    }
}
