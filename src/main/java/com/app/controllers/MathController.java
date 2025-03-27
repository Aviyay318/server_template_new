package com.app.controllers;

import com.app.entities.ExerciseHistoryEntity;
import com.app.entities.QuestionTypeEntity;
import com.app.entities.UserEntity;
import com.app.service.Persist;
import com.app.utils.LevelUp;
import com.app.utils.MathExercise;
import com.app.utils.MultiplicationTable;
import com.app.utils.QuestionGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class MathController {

    @Autowired
    private Persist persist;

    @GetMapping("/get-multiple-exercises")
    public List<Map<String, Object>> getMultipleExercises(String token, int level){
        List<Map<String, Object>> exercises = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            exercises.add(getExercise(token,level));
        }
        return exercises;
    }


    @GetMapping("/get-exercise")
    public Map<String, Object> getExercise(String token, int level) {

        // שליפת משתמש לפי טוקן
        UserEntity user = this.persist.getUserByToken(token);
        if (user == null) {
            throw new RuntimeException("Invalid token");
        }

        // יצירת תרגיל
        MathExercise mathExercise = new MathExercise(level);
        QuestionTypeEntity questionType = this.persist.loadObject(QuestionTypeEntity.class,1);
        // שמירת ההיסטוריה
        ExerciseHistoryEntity exerciseHistory = new ExerciseHistoryEntity(user, level, mathExercise.toString(), false,mathExercise.getSolution().toString(),questionType);
        this.persist.save(exerciseHistory);
        mathExercise.setId(exerciseHistory.getId());
        // החזרת JSON
        return mathExercise.toJson();
    }

    @RequestMapping("/get-level")
    public int getLevel(String token,int questionTypeId){
        UserEntity user = this.persist.getUserByToken(token);
       return LevelUp.getLevelOfUser(this.persist.getExercisesByUserId(user));
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

    @RequestMapping("/get-literal-problem")
    public Map<String, Object> getLiteralProblem(@RequestParam String token) {
        UserEntity user = this.persist.getUserByToken(token);
        System.out.println(user);
        Map<String, Object> literalProblem = QuestionGenerator.generateQuestion();
       System.out.println(literalProblem.get("answer"));
        QuestionTypeEntity questionType = this.persist.loadObject(QuestionTypeEntity.class,2);
        ExerciseHistoryEntity exerciseHistory = new ExerciseHistoryEntity(user,1,(String) literalProblem.get("question"),false, String.valueOf(literalProblem.get("answer")),questionType);
       System.out.println(exerciseHistory.getAnswer());
       this.persist.save(exerciseHistory);
       literalProblem.put("id",exerciseHistory.getId());
        return literalProblem;

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

        // Step 1: מילוי כותרות
        table[0][0] = "X";
        for (int i = 1; i <= 10; i++) {
            table[0][i] = String.valueOf(i); // טור כותרת
            table[i][0] = String.valueOf(i); // שורה כותרת
        }

        // Step 2: מילוי תוצאות נכונות
        for (ExerciseHistoryEntity e : history) {
            if (e.getQuestionType() != null &&
                    e.getQuestionType().getName().equalsIgnoreCase("MULTIPLICATION_TABLE") &&
                    e.getIsCorrectAnswer()) {

                // נניח הפורמט הוא "6 * 2 = ?"
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

        // שליפת משתמש לפי טוקן
        UserEntity user = this.persist.getUserByToken(token);
        if (user == null) {
            throw new RuntimeException("Invalid token");
        }

        // יצירת תרגיל
        MathExercise mathExercise = new MathExercise(level);
        QuestionTypeEntity questionType = this.persist.loadObject(QuestionTypeEntity.class,1);
        // שמירת ההיסטוריה
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
        // החזרת JSON
        return temp;
    }

}
