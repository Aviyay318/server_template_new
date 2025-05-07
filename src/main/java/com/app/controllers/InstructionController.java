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

    @RequestMapping("/get-instruction-by-island-id")
    public String getInstructionByIslandId(@RequestParam int islandId) {
        Object instructionObj = InstructionGenerator.getInstructionById(islandId);

        if (!(instructionObj instanceof Map)) {
            return "<b>שגיאה:</b> לא נמצאו הוראות לאי המבוקש.";
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> instruction = (Map<String, Object>) instructionObj;

        String title = (String) instruction.getOrDefault("title", "אין כותרת");
        String description = (String) instruction.getOrDefault("description", "אין תיאור");
        String tip = (String) instruction.getOrDefault("tip", "אין טיפ");
        String example = ((String) instruction.getOrDefault("example", "אין דוגמה")).replaceAll("\n", "<br>");
        String explanation = (String) instruction.getOrDefault("explanation", null);

        StringBuilder html = new StringBuilder();
        html.append("<b>כותרת:</b> ").append(title).append("<br>");
        html.append("<b>תיאור:</b> ").append(description).append("<br>");
        html.append("<b>טיפ:</b> ").append(tip).append("<br>");
        if (explanation != null) {
            html.append("<b>הסבר נוסף:</b> ").append(explanation.replaceAll("\n", "<br>")).append("<br>");
        }
        html.append("<b>דוגמה:</b><br>").append(example);

        return html.toString();
    }


}

