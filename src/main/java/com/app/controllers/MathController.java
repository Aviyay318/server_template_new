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
        for (int i = 0; i < 25; i++) {
            exercises.add(getExercise(token,level));
        }
        System.out.println(exercises);
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
        System.out.println(temp);
        return temp;
    }


    @GetMapping("/api/svg")
    public List<Map<String, String>> getSvgList() {
        List<Map<String, String>> svgList = new ArrayList<>();

        Map<String, String> item0 = new HashMap<>();
        item0.put("name", "עפרון");
        item0.put("svg", """
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
  <polygon points="10,90 30,70 80,20 90,30 40,80" fill="#FFD700" stroke="#000" stroke-width="2"/>
  <polygon points="10,90 40,80 30,70" fill="#D2691E"/>
</svg>
""");
        svgList.add(item0);

        Map<String, String> item1 = new HashMap<>();
        item1.put("name", "מחברת");
        item1.put("svg", """
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
  <rect x="20" y="10" width="60" height="80" fill="#00CC66" stroke="#000" stroke-width="2"/>
  <line x1="30" y1="20" x2="30" y2="80" stroke="white" stroke-width="2"/>
  <line x1="40" y1="20" x2="40" y2="80" stroke="white" stroke-width="1"/>
  <line x1="50" y1="20" x2="50" y2="80" stroke="white" stroke-width="1"/>
</svg>
""");
        svgList.add(item1);

        Map<String, String> item2 = new HashMap<>();
        item2.put("name", "כובע");
        item2.put("svg", """
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
  <ellipse cx="50" cy="70" rx="30" ry="10" fill="#3399FF"/>
  <path d="M20,70 Q50,30 80,70 Z" fill="#0066CC"/>
</svg>
""");
        svgList.add(item2);

        Map<String, String> item3 = new HashMap<>();
        item3.put("name", "תיק");
        item3.put("svg", """
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
  <rect x="25" y="30" width="50" height="50" rx="10" fill="#FF6633" stroke="#000" stroke-width="2"/>
  <rect x="35" y="20" width="30" height="15" rx="5" fill="#FF9966"/>
</svg>
""");
        svgList.add(item3);

        Map<String, String> item4 = new HashMap<>();
        item4.put("name", "קלמר");
        item4.put("svg", """
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
  <rect x="20" y="40" width="60" height="20" rx="5" fill="#FFCC00" stroke="#000" stroke-width="2"/>
  <circle cx="25" cy="50" r="2" fill="black"/>
</svg>
""");
        svgList.add(item4);

        Map<String, String> item5 = new HashMap<>();
        item5.put("name", "ספר");
        item5.put("svg", """
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
  <path d="M20,20 L45,25 L45,85 L20,80 Z" fill="#FF4444" stroke="#000" stroke-width="2"/>
  <path d="M45,25 L80,20 L80,80 L45,85 Z" fill="#FF8888" stroke="#000" stroke-width="2"/>
</svg>
""");
        svgList.add(item5);

        Map<String, String> item6 = new HashMap<>();
        item6.put("name", "מדבקה");
        item6.put("svg", """
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
  <circle cx="50" cy="50" r="30" fill="#8E44AD"/>
  <text x="50%" y="54%" text-anchor="middle" fill="white" font-size="14" font-family="sans-serif">★</text>
</svg>
""");
        svgList.add(item6);

        Map<String, String> item7 = new HashMap<>();
        item7.put("name", "צעצוע");
        item7.put("svg", """
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
  <circle cx="50" cy="50" r="20" fill="#1ABC9C"/>
  <circle cx="35" cy="35" r="5" fill="#16A085"/>
  <circle cx="65" cy="65" r="5" fill="#16A085"/>
</svg>
""");
        svgList.add(item7);

        Map<String, String> item8 = new HashMap<>();
        item8.put("name", "כדור");
        item8.put("svg", """
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
  <circle cx="50" cy="50" r="30" fill="#E67E22" stroke="#000" stroke-width="2"/>
  <line x1="20" y1="50" x2="80" y2="50" stroke="white" stroke-width="2"/>
  <line x1="50" y1="20" x2="50" y2="80" stroke="white" stroke-width="2"/>
</svg>
""");
        svgList.add(item8);

        Map<String, String> item9 = new HashMap<>();
        item9.put("name", "דגל");
        item9.put("svg", """
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
  <rect x="20" y="20" width="10" height="60" fill="#2ECC71"/>
  <path d="M30,20 L70,30 L30,40 Z" fill="#2ECC71" stroke="#000" stroke-width="1"/>
</svg>
""");
        svgList.add(item9);

        Map<String, String> item10 = new HashMap<>();
        item10.put("name", "חולצה");
        item10.put("svg", """
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
  <path d="M20,30 Q40,10 50,30 Q60,10 80,30 L70,80 H30 L20,30 Z" fill="#3498DB" stroke="#000" stroke-width="2"/>
</svg>
""");
        svgList.add(item10);

//
//        Map<String, String> item11 = new HashMap<>();
//        item11.put("name", "מכנסיים");
//        item11.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#F39C12\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">מכנסיים</text></svg>");
//        svgList.add(item11);
//
//        Map<String, String> item12 = new HashMap<>();
//        item12.put("name", "סוודר");
//        item12.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FFD700\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">סוודר</text></svg>");
//        svgList.add(item12);
//
//        Map<String, String> item13 = new HashMap<>();
//        item13.put("name", "מעיל");
//        item13.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#00CC66\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">מעיל</text></svg>");
//        svgList.add(item13);
//
//        Map<String, String> item14 = new HashMap<>();
//        item14.put("name", "נעל");
//        item14.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#3399FF\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">נעל</text></svg>");
//        svgList.add(item14);
//
//        Map<String, String> item15 = new HashMap<>();
//        item15.put("name", "גרב");
//        item15.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FF6633\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">גרב</text></svg>");
//        svgList.add(item15);
//
//        Map<String, String> item16 = new HashMap<>();
//        item16.put("name", "קופסה");
//        item16.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FFCC00\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">קופסה</text></svg>");
//        svgList.add(item16);
//
//        Map<String, String> item17 = new HashMap<>();
//        item17.put("name", "שקית");
//        item17.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FF4444\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">שקית</text></svg>");
//        svgList.add(item17);
//
//        Map<String, String> item18 = new HashMap<>();
//        item18.put("name", "תמונה");
//        item18.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#8E44AD\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">תמונה</text></svg>");
//        svgList.add(item18);
//
//        Map<String, String> item19 = new HashMap<>();
//        item19.put("name", "בריסטול");
//        item19.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#1ABC9C\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">בריסטול</text></svg>");
//        svgList.add(item19);
//
//        Map<String, String> item20 = new HashMap<>();
//        item20.put("name", "פאזל");
//        item20.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#E67E22\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">פאזל</text></svg>");
//        svgList.add(item20);
//
//        Map<String, String> item21 = new HashMap<>();
//        item21.put("name", "קובייה");
//        item21.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#2ECC71\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">קובייה</text></svg>");
//        svgList.add(item21);
//
//        Map<String, String> item22 = new HashMap<>();
//        item22.put("name", "כרטיס");
//        item22.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#3498DB\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">כרטיס</text></svg>");
//        svgList.add(item22);
//
//        Map<String, String> item23 = new HashMap<>();
//        item23.put("name", "שלט");
//        item23.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#F39C12\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">שלט</text></svg>");
//        svgList.add(item23);
//
//        Map<String, String> item24 = new HashMap<>();
//        item24.put("name", "לוח");
//        item24.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FFD700\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">לוח</text></svg>");
//        svgList.add(item24);
//
//        Map<String, String> item25 = new HashMap<>();
//        item25.put("name", "צבע");
//        item25.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#00CC66\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">צבע</text></svg>");
//        svgList.add(item25);
//
//        Map<String, String> item26 = new HashMap<>();
//        item26.put("name", "טוש");
//        item26.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#3399FF\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">טוש</text></svg>");
//        svgList.add(item26);
//
//        Map<String, String> item27 = new HashMap<>();
//        item27.put("name", "עט");
//        item27.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FF6633\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">עט</text></svg>");
//        svgList.add(item27);
//
//        Map<String, String> item28 = new HashMap<>();
//        item28.put("name", "חוט");
//        item28.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FFCC00\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">חוט</text></svg>");
//        svgList.add(item28);
//
//        Map<String, String> item29 = new HashMap<>();
//        item29.put("name", "מברשת");
//        item29.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FF4444\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">מברשת</text></svg>");
//        svgList.add(item29);
//
//        Map<String, String> item30 = new HashMap<>();
//        item30.put("name", "משקפיים");
//        item30.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#8E44AD\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">משקפיים</text></svg>");
//        svgList.add(item30);
//
//        Map<String, String> item31 = new HashMap<>();
//        item31.put("name", "כפפה");
//        item31.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#1ABC9C\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">כפפה</text></svg>");
//        svgList.add(item31);
//
//        Map<String, String> item32 = new HashMap<>();
//        item32.put("name", "כוכב");
//        item32.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#E67E22\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">כוכב</text></svg>");
//        svgList.add(item32);
//
//        Map<String, String> item33 = new HashMap<>();
//        item33.put("name", "קופסת אוכל");
//        item33.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#2ECC71\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">קופסת אוכל</text></svg>");
//        svgList.add(item33);
//
//        Map<String, String> item34 = new HashMap<>();
//        item34.put("name", "בקבוק שתייה");
//        item34.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#3498DB\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">בקבוק שתייה</text></svg>");
//        svgList.add(item34);
//
//        Map<String, String> item35 = new HashMap<>();
//        item35.put("name", "צמיד");
//        item35.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#F39C12\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">צמיד</text></svg>");
//        svgList.add(item35);
//
//        Map<String, String> item36 = new HashMap<>();
//        item36.put("name", "שרשרת");
//        item36.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FFD700\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">שרשרת</text></svg>");
//        svgList.add(item36);
//
//        Map<String, String> item37 = new HashMap<>();
//        item37.put("name", "עגיל");
//        item37.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#00CC66\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">עגיל</text></svg>");
//        svgList.add(item37);
//
//        Map<String, String> item38 = new HashMap<>();
//        item38.put("name", "חוברת");
//        item38.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#3399FF\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">חוברת</text></svg>");
//        svgList.add(item38);
//
//        Map<String, String> item39 = new HashMap<>();
//        item39.put("name", "פנקס");
//        item39.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FF6633\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">פנקס</text></svg>");
//        svgList.add(item39);
//
//        Map<String, String> item40 = new HashMap<>();
//        item40.put("name", "כיסא");
//        item40.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FFCC00\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">כיסא</text></svg>");
//        svgList.add(item40);
//
//        Map<String, String> item41 = new HashMap<>();
//        item41.put("name", "שולחן");
//        item41.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FF4444\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">שולחן</text></svg>");
//        svgList.add(item41);
//
//        Map<String, String> item42 = new HashMap<>();
//        item42.put("name", "מדף");
//        item42.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#8E44AD\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">מדף</text></svg>");
//        svgList.add(item42);
//
//        Map<String, String> item43 = new HashMap<>();
//        item43.put("name", "מדבקת שם");
//        item43.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#1ABC9C\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">מדבקת שם</text></svg>");
//        svgList.add(item43);
//
//        Map<String, String> item44 = new HashMap<>();
//        item44.put("name", "מספריים");
//        item44.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#E67E22\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">מספריים</text></svg>");
//        svgList.add(item44);
//
//        Map<String, String> item45 = new HashMap<>();
//        item45.put("name", "דבק");
//        item45.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#2ECC71\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">דבק</text></svg>");
//        svgList.add(item45);
//
//        Map<String, String> item46 = new HashMap<>();
//        item46.put("name", "פחית");
//        item46.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#3498DB\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">פחית</text></svg>");
//        svgList.add(item46);
//
//        Map<String, String> item47 = new HashMap<>();
//        item47.put("name", "מחק");
//        item47.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#F39C12\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">מחק</text></svg>");
//        svgList.add(item47);
//
//        Map<String, String> item48 = new HashMap<>();
//        item48.put("name", "מחדד");
//        item48.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FFD700\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">מחדד</text></svg>");
//        svgList.add(item48);
//
//        Map<String, String> item49 = new HashMap<>();
//        item49.put("name", "פנס");
//        item49.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#00CC66\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">פנס</text></svg>");
//        svgList.add(item49);
//
//        Map<String, String> item50 = new HashMap<>();
//        item50.put("name", "שלט מעוצב");
//        item50.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#3399FF\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">שלט מעוצב</text></svg>");
//        svgList.add(item50);
//
//        Map<String, String> item51 = new HashMap<>();
//        item51.put("name", "אריזה");
//        item51.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FF6633\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">אריזה</text></svg>");
//        svgList.add(item51);
//
//        Map<String, String> item52 = new HashMap<>();
//        item52.put("name", "חוברת פעילות");
//        item52.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FFCC00\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">חוברת פעילות</text></svg>");
//        svgList.add(item52);
//
//        Map<String, String> item53 = new HashMap<>();
//        item53.put("name", "קופסת משחק");
//        item53.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FF4444\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">קופסת משחק</text></svg>");
//        svgList.add(item53);
//
//        Map<String, String> item54 = new HashMap<>();
//        item54.put("name", "תמונת נוף");
//        item54.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#8E44AD\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">תמונת נוף</text></svg>");
//        svgList.add(item54);
//
//        Map<String, String> item55 = new HashMap<>();
//        item55.put("name", "דגלון");
//        item55.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#1ABC9C\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">דגלון</text></svg>");
//        svgList.add(item55);
//
//        Map<String, String> item56 = new HashMap<>();
//        item56.put("name", "כרטיסיה");
//        item56.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#E67E22\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">כרטיסיה</text></svg>");
//        svgList.add(item56);
//
//        Map<String, String> item57 = new HashMap<>();
//        item57.put("name", "שאלון");
//        item57.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#2ECC71\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">שאלון</text></svg>");
//        svgList.add(item57);
//
//        Map<String, String> item58 = new HashMap<>();
//        item58.put("name", "סימניה");
//        item58.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#3498DB\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">סימניה</text></svg>");
//        svgList.add(item58);
//
//        Map<String, String> item59 = new HashMap<>();
//        item59.put("name", "פעמון");
//        item59.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#F39C12\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">פעמון</text></svg>");
//        svgList.add(item59);
//
//        Map<String, String> item60 = new HashMap<>();
//        item60.put("name", "בלון");
//        item60.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FFD700\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">בלון</text></svg>");
//        svgList.add(item60);
//
//        Map<String, String> item61 = new HashMap<>();
//        item61.put("name", "שלט יום הולדת");
//        item61.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#00CC66\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">שלט יום הולדת</text></svg>");
//        svgList.add(item61);
//
//        Map<String, String> item62 = new HashMap<>();
//        item62.put("name", "חבל קפיצה");
//        item62.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#3399FF\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">חבל קפיצה</text></svg>");
//        svgList.add(item62);
//
//        Map<String, String> item63 = new HashMap<>();
//        item63.put("name", "שקית הפתעה");
//        item63.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FF6633\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">שקית הפתעה</text></svg>");
//        svgList.add(item63);
//
//        Map<String, String> item64 = new HashMap<>();
//        item64.put("name", "תיבת נגינה");
//        item64.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FFCC00\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">תיבת נגינה</text></svg>");
//        svgList.add(item64);
//
//        Map<String, String> item65 = new HashMap<>();
//        item65.put("name", "פאזל תלת מימד");
//        item65.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FF4444\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">פאזל תלת מימד</text></svg>");
//        svgList.add(item65);
//
//        Map<String, String> item66 = new HashMap<>();
//        item66.put("name", "צעיף");
//        item66.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#8E44AD\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">צעיף</text></svg>");
//        svgList.add(item66);
//
//        Map<String, String> item67 = new HashMap<>();
//        item67.put("name", "כובע יום הולדת");
//        item67.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#1ABC9C\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">כובע יום הולדת</text></svg>");
//        svgList.add(item67);
//
//        Map<String, String> item68 = new HashMap<>();
//        item68.put("name", "קופסת יצירה");
//        item68.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#E67E22\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">קופסת יצירה</text></svg>");
//        svgList.add(item68);
//
//        Map<String, String> item69 = new HashMap<>();
//        item69.put("name", "צבע פנים");
//        item69.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#2ECC71\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">צבע פנים</text></svg>");
//        svgList.add(item69);
//
//        Map<String, String> item70 = new HashMap<>();
//        item70.put("name", "מדבקה תלת מימד");
//        item70.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#3498DB\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">מדבקה תלת מימד</text></svg>");
//        svgList.add(item70);
//
//        Map<String, String> item71 = new HashMap<>();
//        item71.put("name", "משחק");
//        item71.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#F39C12\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">משחק</text></svg>");
//        svgList.add(item71);
//
//        Map<String, String> item72 = new HashMap<>();
//        item72.put("name", "שעון");
//        item72.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FFD700\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">שעון</text></svg>");
//        svgList.add(item72);
//
//        Map<String, String> item73 = new HashMap<>();
//        item73.put("name", "מחשב");
//        item73.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#00CC66\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">מחשב</text></svg>");
//        svgList.add(item73);
//
//        Map<String, String> item74 = new HashMap<>();
//        item74.put("name", "טלפון");
//        item74.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#3399FF\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">טלפון</text></svg>");
//        svgList.add(item74);
//
//        Map<String, String> item75 = new HashMap<>();
//        item75.put("name", "מכשיר קשר");
//        item75.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FF6633\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">מכשיר קשר</text></svg>");
//        svgList.add(item75);
//
//        Map<String, String> item76 = new HashMap<>();
//        item76.put("name", "גיר");
//        item76.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FFCC00\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">גיר</text></svg>");
//        svgList.add(item76);
//
//        Map<String, String> item77 = new HashMap<>();
//        item77.put("name", "לוח ציור");
//        item77.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FF4444\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">לוח ציור</text></svg>");
//        svgList.add(item77);
//
//        Map<String, String> item78 = new HashMap<>();
//        item78.put("name", "טוש מחיק");
//        item78.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#8E44AD\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">טוש מחיק</text></svg>");
//        svgList.add(item78);
//
//        Map<String, String> item79 = new HashMap<>();
//        item79.put("name", "צבע שמן");
//        item79.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#1ABC9C\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">צבע שמן</text></svg>");
//        svgList.add(item79);
//
//        Map<String, String> item80 = new HashMap<>();
//        item80.put("name", "מכחול");
//        item80.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#E67E22\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">מכחול</text></svg>");
//        svgList.add(item80);
//
//        Map<String, String> item81 = new HashMap<>();
//        item81.put("name", "גליל");
//        item81.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#2ECC71\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">גליל</text></svg>");
//        svgList.add(item81);
//
//        Map<String, String> item82 = new HashMap<>();
//        item82.put("name", "כרטיס ברכה");
//        item82.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#3498DB\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">כרטיס ברכה</text></svg>");
//        svgList.add(item82);
//
//        Map<String, String> item83 = new HashMap<>();
//        item83.put("name", "עוגייה");
//        item83.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#F39C12\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">עוגייה</text></svg>");
//        svgList.add(item83);
//
//        Map<String, String> item84 = new HashMap<>();
//        item84.put("name", "ממתק");
//        item84.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FFD700\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">ממתק</text></svg>");
//        svgList.add(item84);
//
//        Map<String, String> item85 = new HashMap<>();
//        item85.put("name", "תפוח");
//        item85.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#00CC66\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">תפוח</text></svg>");
//        svgList.add(item85);
//
//        Map<String, String> item86 = new HashMap<>();
//        item86.put("name", "עוגה");
//        item86.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#3399FF\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">עוגה</text></svg>");
//        svgList.add(item86);
//
//        Map<String, String> item87 = new HashMap<>();
//        item87.put("name", "כריך");
//        item87.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FF6633\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">כריך</text></svg>");
//        svgList.add(item87);
//
//        Map<String, String> item88 = new HashMap<>();
//        item88.put("name", "מטריה");
//        item88.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FFCC00\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">מטריה</text></svg>");
//        svgList.add(item88);
//
//        Map<String, String> item89 = new HashMap<>();
//        item89.put("name", "רמקול");
//        item89.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FF4444\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">רמקול</text></svg>");
//        svgList.add(item89);
//
//        Map<String, String> item90 = new HashMap<>();
//        item90.put("name", "רדיו");
//        item90.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#8E44AD\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">רדיו</text></svg>");
//        svgList.add(item90);
//
//        Map<String, String> item91 = new HashMap<>();
//        item91.put("name", "טלוויזיה");
//        item91.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#1ABC9C\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">טלוויזיה</text></svg>");
//        svgList.add(item91);
//
//        Map<String, String> item92 = new HashMap<>();
//        item92.put("name", "סוללה");
//        item92.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#E67E22\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">סוללה</text></svg>");
//        svgList.add(item92);
//
//        Map<String, String> item93 = new HashMap<>();
//        item93.put("name", "כבל");
//        item93.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#2ECC71\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">כבל</text></svg>");
//        svgList.add(item93);
//
//        Map<String, String> item94 = new HashMap<>();
//        item94.put("name", "מטען");
//        item94.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#3498DB\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">מטען</text></svg>");
//        svgList.add(item94);
//
//        Map<String, String> item95 = new HashMap<>();
//        item95.put("name", "פוסטר");
//        item95.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#F39C12\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">פוסטר</text></svg>");
//        svgList.add(item95);
//
//        Map<String, String> item96 = new HashMap<>();
//        item96.put("name", "כרטיס זיכרון");
//        item96.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FFD700\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">כרטיס זיכרון</text></svg>");
//        svgList.add(item96);
//
//        Map<String, String> item97 = new HashMap<>();
//        item97.put("name", "תקליט");
//        item97.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#00CC66\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">תקליט</text></svg>");
//        svgList.add(item97);
//
//        Map<String, String> item98 = new HashMap<>();
//        item98.put("name", "דיסק");
//        item98.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#3399FF\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">דיסק</text></svg>");
//        svgList.add(item98);
//
//        Map<String, String> item99 = new HashMap<>();
//        item99.put("name", "מדפסת");
//        item99.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FF6633\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">מדפסת</text></svg>");
//        svgList.add(item99);
//
//        Map<String, String> item100 = new HashMap<>();
//        item100.put("name", "עכבר");
//        item100.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FFCC00\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">עכבר</text></svg>");
//        svgList.add(item100);
//
//        Map<String, String> item101 = new HashMap<>();
//        item101.put("name", "מקלדת");
//        item101.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FF4444\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">מקלדת</text></svg>");
//        svgList.add(item101);
//
//        Map<String, String> item102 = new HashMap<>();
//        item102.put("name", "מסך");
//        item102.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#8E44AD\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">מסך</text></svg>");
//        svgList.add(item102);
//
//        Map<String, String> item103 = new HashMap<>();
//        item103.put("name", "אוזניות");
//        item103.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#1ABC9C\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">אוזניות</text></svg>");
//        svgList.add(item103);
//
//        Map<String, String> item104 = new HashMap<>();
//        item104.put("name", "רשימה");
//        item104.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#E67E22\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">רשימה</text></svg>");
//        svgList.add(item104);
//
//        Map<String, String> item105 = new HashMap<>();
//        item105.put("name", "סימון");
//        item105.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#2ECC71\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">סימון</text></svg>");
//        svgList.add(item105);
//
//        Map<String, String> item106 = new HashMap<>();
//        item106.put("name", "קופסת צבע");
//        item106.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#3498DB\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">קופסת צבע</text></svg>");
//        svgList.add(item106);
//
//        Map<String, String> item107 = new HashMap<>();
//        item107.put("name", "פחית צבע");
//        item107.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#F39C12\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">פחית צבע</text></svg>");
//        svgList.add(item107);
//
//        Map<String, String> item108 = new HashMap<>();
//        item108.put("name", "שלט כיתה");
//        item108.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FFD700\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">שלט כיתה</text></svg>");
//        svgList.add(item108);
//
//        Map<String, String> item109 = new HashMap<>();
//        item109.put("name", "ארון");
//        item109.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#00CC66\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">ארון</text></svg>");
//        svgList.add(item109);
//
//        Map<String, String> item110 = new HashMap<>();
//        item110.put("name", "שידה");
//        item110.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#3399FF\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">שידה</text></svg>");
//        svgList.add(item110);
//
//        Map<String, String> item111 = new HashMap<>();
//        item111.put("name", "שמיכה");
//        item111.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FF6633\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">שמיכה</text></svg>");
//        svgList.add(item111);
//
//        Map<String, String> item112 = new HashMap<>();
//        item112.put("name", "כרית");
//        item112.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FFCC00\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">כרית</text></svg>");
//        svgList.add(item112);
//
//        Map<String, String> item113 = new HashMap<>();
//        item113.put("name", "סדין");
//        item113.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FF4444\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">סדין</text></svg>");
//        svgList.add(item113);
//
//        Map<String, String> item114 = new HashMap<>();
//        item114.put("name", "פנס צבעוני");
//        item114.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#8E44AD\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">פנס צבעוני</text></svg>");
//        svgList.add(item114);
//
//        Map<String, String> item115 = new HashMap<>();
//        item115.put("name", "דגל קטן");
//        item115.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#1ABC9C\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">דגל קטן</text></svg>");
//        svgList.add(item115);
//
//        Map<String, String> item116 = new HashMap<>();
//        item116.put("name", "מד טמפרטורה");
//        item116.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#E67E22\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">מד טמפרטורה</text></svg>");
//        svgList.add(item116);
//
//        Map<String, String> item117 = new HashMap<>();
//        item117.put("name", "מד לחות");
//        item117.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#2ECC71\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">מד לחות</text></svg>");
//        svgList.add(item117);
//
//        Map<String, String> item118 = new HashMap<>();
//        item118.put("name", "מצפן");
//        item118.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#3498DB\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">מצפן</text></svg>");
//        svgList.add(item118);
//
//        Map<String, String> item119 = new HashMap<>();
//        item119.put("name", "שק חול");
//        item119.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#F39C12\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">שק חול</text></svg>");
//        svgList.add(item119);
//
//        Map<String, String> item120 = new HashMap<>();
//        item120.put("name", "כדור ספוג");
//        item120.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FFD700\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">כדור ספוג</text></svg>");
//        svgList.add(item120);
//
//        Map<String, String> item121 = new HashMap<>();
//        item121.put("name", "מחבט");
//        item121.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#00CC66\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">מחבט</text></svg>");
//        svgList.add(item121);
//
//        Map<String, String> item122 = new HashMap<>();
//        item122.put("name", "כדורגל");
//        item122.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#3399FF\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">כדורגל</text></svg>");
//        svgList.add(item122);
//
//        Map<String, String> item123 = new HashMap<>();
//        item123.put("name", "מדליה");
//        item123.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FF6633\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">מדליה</text></svg>");
//        svgList.add(item123);
//
//        Map<String, String> item124 = new HashMap<>();
//        item124.put("name", "גביע");
//        item124.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FFCC00\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">גביע</text></svg>");
//        svgList.add(item124);
//
//        Map<String, String> item125 = new HashMap<>();
//        item125.put("name", "כיסא חוף");
//        item125.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FF4444\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">כיסא חוף</text></svg>");
//        svgList.add(item125);
//
//        Map<String, String> item126 = new HashMap<>();
//        item126.put("name", "בריכה");
//        item126.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#8E44AD\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">בריכה</text></svg>");
//        svgList.add(item126);
//
//        Map<String, String> item127 = new HashMap<>();
//        item127.put("name", "גלגל ים");
//        item127.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#1ABC9C\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">גלגל ים</text></svg>");
//        svgList.add(item127);
//
//        Map<String, String> item128 = new HashMap<>();
//        item128.put("name", "שמשיה");
//        item128.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#E67E22\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">שמשיה</text></svg>");
//        svgList.add(item128);
//
//        Map<String, String> item129 = new HashMap<>();
//        item129.put("name", "משקולת");
//        item129.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#2ECC71\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">משקולת</text></svg>");
//        svgList.add(item129);
//
//        Map<String, String> item130 = new HashMap<>();
//        item130.put("name", "אקדח מים");
//        item130.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#3498DB\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">אקדח מים</text></svg>");
//        svgList.add(item130);
//
//        Map<String, String> item131 = new HashMap<>();
//        item131.put("name", "צידנית");
//        item131.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#F39C12\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">צידנית</text></svg>");
//        svgList.add(item131);
//
//        Map<String, String> item132 = new HashMap<>();
//        item132.put("name", "שקית קרח");
//        item132.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FFD700\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">שקית קרח</text></svg>");
//        svgList.add(item132);
//
//        Map<String, String> item133 = new HashMap<>();
//        item133.put("name", "שלט לחדר");
//        item133.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#00CC66\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">שלט לחדר</text></svg>");
//        svgList.add(item133);
//
//        Map<String, String> item134 = new HashMap<>();
//        item134.put("name", "קישוט");
//        item134.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#3399FF\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">קישוט</text></svg>");
//        svgList.add(item134);
//
//        Map<String, String> item135 = new HashMap<>();
//        item135.put("name", "תבלין");
//        item135.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FF6633\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">תבלין</text></svg>");
//        svgList.add(item135);
//
//        Map<String, String> item136 = new HashMap<>();
//        item136.put("name", "שלט לדלת");
//        item136.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FFCC00\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">שלט לדלת</text></svg>");
//        svgList.add(item136);
//
//        Map<String, String> item137 = new HashMap<>();
//        item137.put("name", "נר");
//        item137.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FF4444\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">נר</text></svg>");
//        svgList.add(item137);
//
//        Map<String, String> item138 = new HashMap<>();
//        item138.put("name", "הזמנה");
//        item138.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#8E44AD\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">הזמנה</text></svg>");
//        svgList.add(item138);
//
//        Map<String, String> item139 = new HashMap<>();
//        item139.put("name", "מדבקת קיר");
//        item139.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#1ABC9C\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">מדבקת קיר</text></svg>");
//        svgList.add(item139);
//
//        Map<String, String> item140 = new HashMap<>();
//        item140.put("name", "צבע גואש");
//        item140.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#E67E22\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">צבע גואש</text></svg>");
//        svgList.add(item140);
//
//        Map<String, String> item141 = new HashMap<>();
//        item141.put("name", "שבלונה");
//        item141.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#2ECC71\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">שבלונה</text></svg>");
//        svgList.add(item141);
//
//        Map<String, String> item142 = new HashMap<>();
//        item142.put("name", "שלט ברוכים הבאים");
//        item142.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#3498DB\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">שלט ברוכים הבאים</text></svg>");
//        svgList.add(item142);
//
//        Map<String, String> item143 = new HashMap<>();
//        item143.put("name", "בריסטול צבעוני");
//        item143.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#F39C12\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">בריסטול צבעוני</text></svg>");
//        svgList.add(item143);
//
//        Map<String, String> item144 = new HashMap<>();
//        item144.put("name", "סרט צבעוני");
//        item144.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#FFD700\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">סרט צבעוני</text></svg>");
//        svgList.add(item144);
//
//        Map<String, String> item145 = new HashMap<>();
//        item145.put("name", "קופסת מתנה");
//        item145.put("svg", "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\" viewBox=\"0 0 100 100\"><rect width=\"100\" height=\"100\" rx=\"20\" ry=\"20\" fill=\"#00CC66\"/><text x=\"50%\" y=\"55%\" dominant-baseline=\"middle\" text-anchor=\"middle\" fill=\"white\" font-size=\"12\" font-family=\"sans-serif\">קופסת מתנה</text></svg>");
//        svgList.add(item145);
//
//        for (Map<String, String> item : svgList) {
//            System.out.println("שם: " + item.get("name"));
//            System.out.println("SVG: " + item.get("svg"));
//            System.out.println();
//        }

        return svgList;
    }


}
