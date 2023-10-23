package com.konkuk.vecto.likes.controller;

import com.konkuk.vecto.likes.service.LikesService;
import com.konkuk.vecto.security.config.argumentresolver.UserInfo;
import com.konkuk.vecto.security.model.common.codes.ResponseCode;
import com.konkuk.vecto.security.model.common.codes.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LikesController {
    LikesService likesService;

    @PostMapping("/feed/{feedId}/likes")
    public ResponseEntity<ResponseCode<String>> likes(@PathVariable Long feedId, @UserInfo String userId){
        likesService.saveLikes(feedId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseCode<>(SuccessCode.INSERT));
    }

    @DeleteMapping("/feed/{feedId}/likes")
    public ResponseEntity<ResponseCode<String>> unLikes(@PathVariable Long feedId, @UserInfo String userId){
        likesService.deleteLikes(feedId, userId);
        return ResponseEntity.ok().body(new ResponseCode<>(SuccessCode.DELETE));
    }
}
