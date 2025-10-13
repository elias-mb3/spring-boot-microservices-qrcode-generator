package com.monteiroelias.qrcode_generator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.monteiroelias.qrcode_generator.dto.QrCodeGenerateRequest;
import com.monteiroelias.qrcode_generator.dto.QrCodeGenerateResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/qrcode")
public class QrCodeController {

    @PostMapping("path")
    public ResponseEntity<QrCodeGenerateResponse> generate(@RequestBody QrCodeGenerateRequest request) {
        return null;
        //TODO: process POST request
 
    }
    
   
}
