package com.app.controllers;

import com.app.entities.*;
import com.app.service.Persist;
import com.app.utils.LevelUp;
import com.app.utils.MathExercise;
import com.app.utils.MultiplicationTable;
import com.app.utils.QuestionGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;

@RestController
public class MathController {

    @Autowired
    private Persist persist;

    @GetMapping("/get-multiple-exercises")
    public List<Map<String, Object>> getMultipleExercises(String token, int level){
        List<Map<String, Object>> exercises = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            exercises.add(getExercise(token,level));
        }
        return exercises;
    }


    @GetMapping("/get-exercise")
    public Map<String, Object> getExercise(String token, int level) {


        UserEntity user = this.persist.getUserByToken(token);
        if (user == null) {
            throw new RuntimeException("Invalid token");
        }


        MathExercise mathExercise = new MathExercise(level);
        QuestionTypeEntity questionType = this.persist.loadObject(QuestionTypeEntity.class,1);
        ExerciseHistoryEntity exerciseHistory = new ExerciseHistoryEntity(user, level, mathExercise.toString(), false,mathExercise.getSolution().toString(),questionType);
        this.persist.save(exerciseHistory);
        mathExercise.setId(exerciseHistory.getId());
        return mathExercise.toJson();
    }


    @RequestMapping("/check-exercise")
    public boolean checkExercise(String token, int id,String answer){
        ExerciseHistoryEntity exerciseHistory = this.persist.loadObject(ExerciseHistoryEntity.class,id);
        if (exerciseHistory.getAnswer().equals(answer)) {
            exerciseHistory.setCorrectAnswer(true);
            this.persist.save(exerciseHistory);
            return true;
        }
        return false;
    }



    @RequestMapping("/check-literal-problem")
    public boolean checkLiteralProblem(String token, int id){
        return false;
    }

    @RequestMapping("/get-question-type")
    public List<QuestionTypeEntity> getQuestionType(){
        return this.persist.loadList(QuestionTypeEntity.class);
    }
    @RequestMapping("/get-multiplication-table-exercise")
    public MultiplicationTable getMultiplicationTableExercise(){
        return new MultiplicationTable();
    }

    @RequestMapping("/get-multiplication-table-history")
    @ResponseBody
    public Map<String, Object> getMultiplicationTableHistory(@RequestParam String token) {
        UserEntity user = this.persist.getUserByToken(token);
        List<ExerciseHistoryEntity> history = this.persist.getExercisesByUserId(user);

        String[][] table = new String[11][11]; // [row][column]

        table[0][0] = "X";
        for (int i = 1; i <= 10; i++) {
            table[0][i] = String.valueOf(i);
            table[i][0] = String.valueOf(i);
        }

        for (ExerciseHistoryEntity e : history) {
            if (e.getQuestionType() != null &&
                    e.getQuestionType().getName().equalsIgnoreCase("MULTIPLICATION_TABLE") &&
                    e.getIsCorrectAnswer()) {

                String[] parts = e.getExercise().split("\\*|=");
                if (parts.length >= 2) {
                    int a = Integer.parseInt(parts[0].trim());
                    int b = Integer.parseInt(parts[1].trim());

                    if (a >= 1 && a <= 10 && b >= 1 && b <= 10) {
                        table[a][b] = String.valueOf(a * b);
                    }
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("table", table);
        return result;
    }

    @GetMapping("/get-exercise-with-option")
    public Map<String, Object> getExerciseWithOption(String token, int level) {

        UserEntity user = this.persist.getUserByToken(token);
        if (user == null) {
            throw new RuntimeException("Invalid token");
        }

        MathExercise mathExercise = new MathExercise(level);
        QuestionTypeEntity questionType = this.persist.loadObject(QuestionTypeEntity.class,1);
        ExerciseHistoryEntity exerciseHistory = new ExerciseHistoryEntity(user, level, mathExercise.toString(), false,mathExercise.getSolution().toString(),questionType);
        this.persist.save(exerciseHistory);
        mathExercise.setId(exerciseHistory.getId());
        Map<String, Object> temp = mathExercise.toJson();
        List<Integer> array = new ArrayList<>();
        array.add(0,mathExercise.getSolution());
        for (int i = 1; i < 4; i++) {
            array.add(new Random().nextInt(mathExercise.getSolution()-5,mathExercise.getSolution()+5))  ;
        }
        temp.put("option",array);
        return temp;
    }


    @GetMapping("/api/svg")
    public List<ObjectsEntity> getSvgList() {
       return this.persist.loadList(ObjectsEntity.class);
     
    }

    @PostConstruct
    public void init() {
      //  UserEntity user = this.persist.getUserByToken("7727B54FDA784AB0EC24645009CD880D");
//        Objects svgList = getObjects();
//        for (Map<String, String> item : svgList.getSvgList()) {
//            ObjectsEntity objectsEntity = new ObjectsEntity();
//            objectsEntity.setSingularName(item.get("name"));
//            objectsEntity.setSvg(item.get("svg"));
//
//            this.persist.save(objectsEntity);
//        }
    }


}
