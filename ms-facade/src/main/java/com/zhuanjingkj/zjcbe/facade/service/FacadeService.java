package com.zhuanjingkj.zjcbe.facade.service;

import com.zhuanjingkj.zjcbe.common.AppConst;
import com.zhuanjingkj.zjcbe.data.dto.GetUserInfoDTO;
import com.zhuanjingkj.zjcbe.data.dto.LoginDTO;
import com.zhuanjingkj.zjcbe.data.dto.ResultDTO;
import com.zhuanjingkj.zjcbe.data.rto.LoginRTO;
import com.zhuanjingkj.zjcbe.facade.fcc.FccUserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class FacadeService implements IFacadeService {
    @Autowired
    FccUserService fccUserService;

    @Override
    public ResultDTO<LoginDTO> login(LoginRTO rto) {
        System.out.println("ms-facade::FacadeService.login 1");
        ResultDTO<LoginDTO> dto = fccUserService.login(rto);
        System.out.println("ms-facade::FacadeService.login 2");
        LoginDTO data = (LoginDTO)dto.getData();
        data.setJwtToken(generateJwtToken(data));
        System.out.println("ms-facade::FacadeService.login 3");
        return dto;
    }

    @Override
    public ResultDTO<GetUserInfoDTO> getUserInfo(String platform, String version, String userIdStr) {
        return fccUserService.getUserInfo(platform, version, userIdStr);
    }

    public String generateJwtToken(LoginDTO data) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", "" + data.getUserId());
        Long endTime = System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 15;
        String token = Jwts.builder().setSubject("zjc").setExpiration(new Date(endTime))
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, AppConst.JWT_KEY.getBytes()).compact();
        System.out.println("token=" + token + "!");
        return token;
    }
}
