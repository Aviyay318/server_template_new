package com.app.controllers;

import com.app.MathIslands.*;
import com.app.entities.ExerciseHistoryEntity;
import com.app.entities.IslandsEntity;
import com.app.entities.LevelsEntity;
import com.app.entities.UserEntity;
import com.app.responses.CheckExerciseResponse;
import com.app.service.Persist;
import com.app.utils.Constants;
import com.app.utils.IslandUtils;
import com.app.utils.LevelUp;
import org.apache.tomcat.jni.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.app.Math.Fraction;
import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

import static com.app.utils.LevelUp.calculateLevelProgress;

@RestController
@RequestMapping("/api/islands")
public class IslandsController {

    @Autowired
    private Persist persist;

    @Autowired
    private AddAndSubIslandService addAndSubIslandService;

    @Autowired
    private MultiplicationIslandService multiplicationIslandService;

    @Autowired
    private DivisionIslandService divisionIslandService;
    @Autowired
    private FloatingPointIslandService floatingPointIslandService;
    @Autowired
    private LongAddAndSubIslandService longAddAndSubIslandService;

    @RequestMapping("/Addition-and-subtraction")
    public Map<String, Object> addAndSub(@RequestParam String token, @RequestParam int questionType) {
        UserEntity user = this.persist.getUserByToken(token);
        IslandsEntity island = this.persist.loadObject(IslandsEntity.class,Constants.ADD_SUB_ISLAND);
        LevelsEntity islandLevel = this.persist.getLevelByUserIdAndIslandId(user,island);
        return addAndSubIslandService.generateExercise(user,island, islandLevel, questionType);
    }
    @RequestMapping("/multiplication")
    public Map<String, Object> multiplicationTable(@RequestParam String token, @RequestParam int questionType) {

        UserEntity user = this.persist.getUserByToken(token);
        IslandsEntity island = this.persist.loadObject(IslandsEntity.class,Constants.MULTIPLICATION_ISLAND);
        System.out.println(island);
        LevelsEntity islandLevel = this.persist.getLevelByUserIdAndIslandId(user,island);
        System.out.println(islandLevel);
        return multiplicationIslandService.generateExercise(user,island, islandLevel, questionType);
    }
@RequestMapping("/division")
    public Map<String, Object> divisionIsland(@RequestParam String token, @RequestParam int questionType) {
        UserEntity user = persist.getUserByToken(token);
        IslandsEntity island = persist.loadObject(IslandsEntity.class, Constants.DIVISION_ISLAND);
        LevelsEntity level = persist.getLevelsByUserId(user).stream()
                .filter(l -> l.getIsland().getId() == island.getId())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Level not found for user in division island"));

    return divisionIslandService.generateExercise(user, island, level, questionType);
    }

    @RequestMapping("/floating-point")
    public Map<String, Object> floatingPointIsland(@RequestParam String token, @RequestParam int questionType) {
        UserEntity user = persist.getUserByToken(token);
        IslandsEntity island = persist.loadObject(IslandsEntity.class,Constants.ISLAND_DECIMALS_AND_FRACTIONS );
        LevelsEntity level = persist.getLevelsByUserId(user).stream()
                .filter(l -> l.getIsland().getId() == island.getId())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Level not found for user in floating-point island"));

        return floatingPointIslandService.generateExercise(user, island, level, questionType);
    }

    @RequestMapping("/long-addition-and-subtraction")
    public Map<String, Object> longAdditionAndSubtractionIsland(@RequestParam String token, @RequestParam int questionType) {
        UserEntity user = this.persist.getUserByToken(token);
        IslandsEntity island = this.persist.loadObject(IslandsEntity.class, Constants.ISLAND_LONG_ADDITION_AND_SUBTRACTION);
        LevelsEntity islandLevel = this.persist.getLevelByUserIdAndIslandId(user, island);
        return longAddAndSubIslandService.generateExercise(user, island, islandLevel, questionType);
    }
    //עדין לא הספקנו את 3 האיים האחרונים (כי החלנו אחרי פסח) אבל בחופש נוסיף
    //, אבל יש גרסה ראשונים מוכנה ואפשר עליה תמיד להוסיף שלבים
    //בנינו בסיס טוב להוספת שלבים
    @RequestMapping("/long-multiplication-and-division")
    public Map<String, Object> longMultiplicationAndDivisionIsland(@RequestParam String token, @RequestParam int questionType) {
        return null;
    }
    @RequestMapping("/mixed-challenge-island")
    public Map<String, Object> mixedChallengeIsland(@RequestParam String token, @RequestParam int questionType) {
        return null;
    }
    @RequestMapping("/equations-island")
    public Map<String, Object> equationsIsland(@RequestParam String token, @RequestParam int questionType) {
        return null;
    }


    @RequestMapping("/check-exercise")
    public CheckExerciseResponse checkExercise(
            @RequestParam String token,
            @RequestParam int exerciseId,
            @RequestParam String answer,
            @RequestParam int solution_time,
            @RequestParam boolean usedClue,
            @RequestParam int questionType) {

        System.out.println("=== START checkExercise ===");
        System.out.println("Received params -> token: " + token + ", exerciseId: " + exerciseId +
                ", answer: " + answer + ", solution_time: " + solution_time +
                ", usedClue: " + usedClue + ", questionType: " + questionType);

        boolean success = false;
        String message = "wrong question id";
        int score = 1;
        int level = 1;
        String islandOpen = null;
        double progress = 0;
        UserEntity user = null;
        LevelsEntity islandLevel = null;
        String levelUp = null;

        try {
            user = this.persist.getUserByToken(token);
            ExerciseHistoryEntity exerciseHistory = this.persist.getExerciseByExerciseId(exerciseId);

            if (exerciseHistory == null || user == null) {
                System.out.println("Invalid exercise or user.");
                return new CheckExerciseResponse(false, "Invalid token or exercise", null, null, islandLevel, 0.0, null);
            }

            System.out.println("Stored answer in DB: " + exerciseHistory.getAnswer());

            boolean isCorrect;
            try {
                String correct = exerciseHistory.getAnswer();
                isCorrect = areEqualFractionsOrDecimals(correct, answer);
            } catch (Exception e) {
                isCorrect = false;
            }

            if (questionType == Constants.COMPLETE_TABLE || isCorrect) {

                System.out.println("Answer matched or questionType is COMPLETE_TABLE");

                exerciseHistory.setCorrectAnswer(true);
                exerciseHistory.setSolutionTime(solution_time);
                this.persist.save(exerciseHistory);

                if (!usedClue) {
                    if (questionType == Constants.LITERAL_PROBLEMS) {
                        score = Constants.LITERAL_SCORE_NO_CLUE;
                    } else if (questionType == Constants.COMPLETE_TABLE && solution_time < 120) {
                        score = Constants.COMPLETE_TABLE_FAST_SCORE;
                    } else {
                        score = Constants.BASE_SCORE;
                    }
                } else {
                    score = Constants.SCORE_WITH_CLUE;
                }

                success = true;
                message = "Great job!";
            } else {
                System.out.println("Answer is incorrect.");
                score = Constants.WRONG_ANSWER_PENALTY;
                message = "Wrong answer.";
            }

            score += user.getScore();
            user.setScore(score);
            this.persist.save(user);

            IslandsEntity island = this.persist.loadObject(IslandsEntity.class, exerciseHistory.getIslands().getId());
            islandLevel = this.persist.getLevelByUserIdAndIslandId(user, island);

            List<ExerciseHistoryEntity> history = this.persist.getExercisesByUserIdAndIsland(user, island);
            int currentLevel = islandLevel.getLevel();

            List<ExerciseHistoryEntity> currentLevelHistory = history.stream()
                    .filter(e -> e.getLevel() == currentLevel)
                    .collect(Collectors.toList());

            if (questionType == Constants.COMPLETE_TABLE) {
                System.out.println("Question is COMPLETE_TABLE, solution time: " + solution_time);
                if (solution_time < 120) {
                    progress = 1.5 + islandLevel.getProgress();
                } else {
                    progress = 1 + islandLevel.getProgress();
                }
            } else {
                Map<String, Object> levelDetails = calculateLevelProgress(currentLevelHistory, currentLevel, islandLevel.getProgress());
                System.out.println("Level calculation: " + levelDetails);
                level = (int) levelDetails.get("calculatedLevel");
                progress = (double) levelDetails.get("progressToNextLevel");
                levelUp = (String) levelDetails.get("statusMessage");
                islandLevel.setLevel(level);
            }

            if (progress >= 100) {
                islandLevel.setProgress(0);
            } else {
                islandLevel.setProgress(progress);
            }

            this.persist.save(islandLevel);
            islandOpen = openIslands(score, user);

        } catch (Exception e) {
            System.out.println("Exception occurred:");
            e.printStackTrace();
        }

        System.out.println("Result: success=" + success + ", message=" + message + ", progress=" + progress);
        System.out.println("=== END checkExercise ===");
        return new CheckExerciseResponse(success, message, user, islandOpen, islandLevel, progress, levelUp);
    }

    private boolean areEqualFractionsOrDecimals(String a, String b) {
        try {
            if (a.contains("/") || b.contains("/")) {
                Fraction f1 = parseFraction(a);
                Fraction f2 = parseFraction(b);
                return Math.abs(f1.doubleValue() - f2.doubleValue()) < 0.01;
            } else {
                double d1 = Double.parseDouble(a);
                double d2 = Double.parseDouble(b);
                return Math.abs(d1 - d2) < 0.01;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private Fraction parseFraction(String input) {
        if (input.equals("1")) return new Fraction(1, 1);
        String[] parts = input.split("/");
        int num = Integer.parseInt(parts[0].trim());
        int den = parts.length > 1 ? Integer.parseInt(parts[1].trim()) : 1;
        return new Fraction(num, den);
    }


    private String openIslands(int score, UserEntity user) {
        String islandOpen = null;
        List<IslandsEntity> allIslands = this.persist.loadList(IslandsEntity.class);
        List<LevelsEntity> userLevels = this.persist.getLevelsByUserId(user);

        for (IslandsEntity island : allIslands) {
            if (score >= island.getScore()) {
                boolean alreadyUnlocked = userLevels.stream()
                        .anyMatch(level -> level.getIsland().getId()==(island.getId()));
                if (!alreadyUnlocked) {
                    LevelsEntity newLevel = new LevelsEntity(user, island, 1);
                    this.persist.save(newLevel);
                    islandOpen =  island.getName() ;
                    System.out.println("Island " + island.getName() + " unlocked for user " + user.getUsername());
                }
            }
        }
        return islandOpen;
    }
    @RequestMapping("/get-user-open-island")
    public List<Map<String, Object>> getUserOpenIsland(@RequestParam String token) {
        return IslandUtils.getIslandsWithOpenStatus(persist, token);
    }



    @RequestMapping("/get-level-by-island")
    public int getLevelByIsland(String token,int islandId){
        UserEntity user = this.persist.getUserByToken(token);
        IslandsEntity island = this.persist.loadObject(IslandsEntity.class,islandId);
        LevelsEntity level =  this.persist.getLevelByUserIdAndIslandId(user,island);
        return level.getLevel();
    }
    @RequestMapping("/get-level-by-user")
    public List<LevelsEntity>  getLevelByUser(String token){
        UserEntity user = this.persist.getUserByToken(token);
        List<LevelsEntity> levels =  this.persist.getLevelsByUserId(user);
        return levels;
    }

     @RequestMapping("/islands")
     public List<IslandsEntity> getIslands(){
        return this.persist.loadList(IslandsEntity.class);
     }
     @RequestMapping("/get-level-of-island")
     public LevelsEntity getLevelOfIsland(
             @RequestParam String token,
             @RequestParam int islandId){
        UserEntity user = this.persist.getUserByToken(token);
        IslandsEntity island = this.persist.loadObject(IslandsEntity.class,islandId);
        LevelsEntity level = this.persist.getLevelByUserIdAndIslandId(user,island);
        return level;
     }

    @RequestMapping("/progress")
    public Map<String, Object> getLevelProgress(
            @RequestParam String token,
            @RequestParam int level,
            @RequestParam int islandId
    ) {   Map<String, Object> progressMap = new HashMap<>();
        try {
            UserEntity user = this.persist.getUserByToken(token);

            IslandsEntity island = this.persist.loadObject(IslandsEntity.class, islandId);
            LevelsEntity islandLevel = this.persist.getLevelByUserIdAndIslandId(user, island);

            List<ExerciseHistoryEntity> history = this.persist.getExercisesByUserIdAndIsland(user, island);

            int currentLevel = islandLevel.getLevel();
            List<ExerciseHistoryEntity> currentLevelHistory = history.stream()
                    .filter(e -> e.getLevel() == currentLevel)
                    .collect(Collectors.toList());

            Map<String, Object> levelDetails = calculateLevelProgress(currentLevelHistory, currentLevel,islandLevel.getProgress());

            level = (int) levelDetails.get("calculatedLevel");
            double progress = (double) levelDetails.get("progressToNextLevel");
            String levelUp = (String) levelDetails.get("statusMessage");

            progressMap.put("progress",progress);
            progressMap.put("levelUp",levelUp);
            progressMap.put("level",level);

        } catch (Exception e) {
            e.printStackTrace();

            return Map.of(
                    "level", level,
                    "progress", 0.0,
                    "levelUp", null
            );
        }
        return progressMap;
    }

    @PostConstruct
    public void init() {
//       System.out.println(multiplicationTable("C34B1B2B6FCFC8307CE1A78006DD6A0E",4));
    //   System.out.println(checkExercise(14,"5",10));
    }
}
