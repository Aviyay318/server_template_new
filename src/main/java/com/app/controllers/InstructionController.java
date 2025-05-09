package com.app.controllers;

import com.app.utils.InstructionGenerator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController

public class InstructionController {

    @RequestMapping("/get-instruction-of-game")
    public Map<String, Object> getInstructionOfGame(){
        return InstructionGenerator.getUserInstructionsJson();
    }

    @RequestMapping("/get-instruction-by-island-id")
    public Map<String, Object> getInstructionByIslandId(@RequestParam int islandId) {
        Object instructionObj = InstructionGenerator.getInstructionById(islandId);

        if (!(instructionObj instanceof Map)) {
            return Map.of(
                    "error", "שגיאה: לא נמצאו הוראות לאי המבוקש."
            );
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> instruction = (Map<String, Object>) instructionObj;

        Map<String, Object> result = new HashMap<>();
        result.put("title", instruction.getOrDefault("title", "אין כותרת"));
        result.put("description", instruction.getOrDefault("description", "אין תיאור"));
        result.put("tip", instruction.getOrDefault("tip", "אין טיפ"));
        result.put("example", ((String) instruction.getOrDefault("example", "אין דוגמה")).replaceAll("\n", "<br>"));
        result.put("explanation", instruction.getOrDefault("explanation", null));

        return result;
    }


}

