package br.com.ipgest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ipgest.model.Igreja;

@RestController
@RequestMapping("/api/igrejas")
public class IgrejaController extends BaseController<Igreja, Long> {
    
}
