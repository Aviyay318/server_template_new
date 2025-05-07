package com.app.controllers;

import com.app.utils.InstructionGenerator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController

public class InstructionController {

    @RequestMapping("/get-instruction-of-game")
    public String getInstructionOfGame(){
        return InstructionGenerator.getUserInstructions();
    }
}

