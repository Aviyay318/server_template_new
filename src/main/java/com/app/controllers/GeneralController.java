package com.app.controllers;

import com.app.entities.*;
import com.app.responses.BasicResponse;
import com.app.responses.LoginResponse;
import com.app.responses.OtpResponse;
import com.app.responses.RegisterResponse;
import com.app.service.Persist;
import com.app.utils.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class GeneralController {
    private HashMap<String,UserEntity> unverifiedUsers = new HashMap<>();
    private List<String> loggedUsers = new ArrayList<>();
     public static final String ADMIN = "admin";
    @Autowired
    private Persist persist;


    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public Object hello() {
        return "Hello From Server";
    }

    @PostConstruct
    public void init(){
        //  enterInformationToTable();
//        for (int i = 0; i < DataGenerator.boyNames.length; i++) {
//            ChildrenNameEntity childrenName = new ChildrenNameEntity();
//            childrenName.setName(DataGenerator.boyNames[i]);
//            childrenName.setGender("male");
//            this.persist.save(childrenName);
//        }
//        for (int i = 0; i < DataGenerator.girlNames.length; i++) {
//            ChildrenNameEntity childrenName = new ChildrenNameEntity();
//            childrenName.setName(DataGenerator.girlNames[i]);
//            childrenName.setGender("female");
//            this.persist.save(childrenName);
//        }
//        QuestionTypeEntity questionType = new QuestionTypeEntity();
//        questionType.setId(1);
//        MathExerciseNew mathExerciseNew = new MathExerciseNew(1,questionType,false);
//        System.out.println("h"+mathExerciseNew.getExercise());
//        בקופסה יש 15 עפרונות. נוספו עוד 12 עפרונות. כמה עפרונות יש בקופסה כעת?"
//	"על המדף יש 40 ספרים. 18 ספרים הוסרו. כמה ספרים נשארו על המדף?"

//        1,פעולות חשבון
//        2,בעיות מילוליות
//        3,משוואות
//        4,לוח הכפל

    }

    private void enterInformationToTable() {
        if (this.persist.loadList(QuestionTypeEntity.class).isEmpty()){
            for (int i = 0; i < Constants.QUESTION_TYPE.length; i++) {
                this.persist.save(Constants.QUESTION_TYPE[i]);
            }
        }
    }
    @RequestMapping("/forgotten-password")
    public BasicResponse forgottenPassword(@RequestParam String email) {
        String message = "האימייל לא קיים במערכת";
        System.out.println("email param: " + email);
        UserEntity user = this.persist.getUserByEmail(email);
        System.out.println(user);
        boolean success = false;
        String otp = null;
        if (user!=null){
            otp = GeneralUtils.generateOtp();
            System.out.println(otp);
            user.setOtp(otp);
            this.persist.save(user);
            success =true;
            message=null;
            boolean emailSent = ApiEmailProcessor.sendEmail(email, "שחזור סיסמא", "Here is your code: " + otp);
            System.out.println("14 : " + emailSent);
        }
        System.out.println(success + message);
        return new BasicResponse(success,message);
    }
//
//    @RequestMapping("/check-otp")
//    public boolean checkOtp(String email,String otp){
//        UserEntity user = this.persist.getUserByEmail(email);
//        boolean success = false;
//        if (user!=null){
//          if (user.getOtp().equals(otp)){
//              success = true;
//          }
//        }
//        return success;
//    }

    @RequestMapping("/recovery-password")
    public BasicResponse recoveryPassword(String email, String password){
        String message = "האימייל לא קיים במערכת";
        UserEntity user = this.persist.getUserByEmail(email);
        boolean success = false;
        System.out.println(password);
        if (user!=null){
            String hashed = GeneralUtils.hashMd5(email, password);
            user.setPassword(hashed);
            this.persist.save(user);
            success = true;
            message = null;
            System.out.println(hashed);
        }
        return new BasicResponse(success,message);
    }


    @PostMapping("/register")
    public RegisterResponse registerUser (@RequestBody UserEntity user) {
        boolean success = false;
        Integer errorCode = Constants.EMAIL_EXIST;
        System.out.println("user" + user);
        System.out.println(user.getPassword());
        String otp = null;
        System.out.println(persist.getUserByEmail(user.getEmail()));
        if (persist.getUserByEmail(user.getEmail()) == null) {
            otp = GeneralUtils.generateOtp();
            user.setOtp(otp);
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                System.out.println("Password is: " + user.getPassword());
            } else{
                String hashed = GeneralUtils.hashMd5(user.getEmail(), user.getPassword());
                user.setPassword(hashed);
                unverifiedUsers.put(user.getEmail(),user);
                boolean emailSent = ApiEmailProcessor.sendEmail(user.getEmail(), "וד אימות להרשמה", "הקוד שלך הוא: " + otp);
                System.out.println("OTP sent: " + emailSent);
                success = true;
                errorCode=null;
            }
        } else{
            System.out.println("something went wrong, or email already exist");
        }
        return new RegisterResponse(success,errorCode,otp);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        boolean success = false;
        String token = null;
        boolean isAdmin = false;

        String hashed = GeneralUtils.hashMd5(email, password);
        UserEntity user = this.persist.getUserByEmailAndPassword(email);

        if (user != null && user.getPassword().equals(hashed)) {
            success = true;
            token = hashed;
            this.loggedUsers.add(token);
            if (Constants.ADMIN_EMAIL.equals(email)) {
                isAdmin = true;
            }

            String otp = GeneralUtils.generateOtp();
            user.setOtp(otp);
            this.persist.save(user); // or update
            boolean emailSent = ApiEmailProcessor.sendEmail(email, "קוד אימות להתחברות", "הקוד שלך הוא: " + otp);
            System.out.println("OTP sent: " + emailSent);
        }else if (user != null&&user.getUsername().equals(ADMIN)) {
            success = true;
            String otp = "123456";
            user.setOtp(otp);
            this.persist.save(user); // or update
            System.out.println(user.getUsername());
            token = user.getPassword();
        }

        return new LoginResponse(success, token, isAdmin);
    }

    @RequestMapping("/get-logged-users")
    public List<UserEntity> getLoggedUsers(String token){
        List<UserEntity> loggedUsers = new ArrayList<>();
        UserEntity user = this.persist.getUserByToken(token);
        if (user.getUsername().equals(ADMIN)){
            for (int i = 0; i < this.loggedUsers.size(); i++) {
               loggedUsers.add(this.persist.getUserByToken(this.loggedUsers.get(i)));
            }
        }
        System.out.println(loggedUsers.size());
        return loggedUsers;
    }

    @RequestMapping("/logout")
    public boolean logout(String token){
        boolean isLogout = false;
        System.out.println(token);
        if (!this.loggedUsers.stream().filter(user-> user.equals(token)).toList().isEmpty()){
            this.unverifiedUsers.remove(token);
            isLogout = true;
        }
        return isLogout;
    }

    @PostMapping ("/check-otp-to-register")
    public OtpResponse getRegisterOtp(@RequestBody OtpRequest otpRequest) {
        System.out.println(otpRequest);
        String email = otpRequest.getEmail();
        String otp = otpRequest.getOtp();
        UserEntity user = unverifiedUsers.get(email);
        if (user == null){
            return new OtpResponse(false, "User not found", false);
        }
        if (user.getOtp() != null && user.getOtp().equals(otp)){
            user.setOtp(null);
            persist.update(user);
            LevelsEntity userLevel = new LevelsEntity();
            userLevel.setIsland(this.persist.loadObject(IslandsEntity.class,Constants.ADD_SUB_ISLAND));
            userLevel.setUser(user);
            userLevel.setLevel(1);
            System.out.println(userLevel);
           this.persist.save(userLevel);
            String message = String.format("היי %s,ואם אתה שי גבעתי אנחנו יודעים איפה אתה גר למקרה ולא נקבל 100 חחחחחח מזל טוב להצטרפותך לאתר לימוד חשבון! אני הולכת לבדוק אתכם ב-7 עיניים!!! חחחחח. אם לא תלמדו, תתחרטו.", user.getUsername());
            boolean emailSent = ApiEmailProcessor.sendEmail(user.getEmail(),"הצטרפות לאתר הכי מושלם שמלמד חשבון",message);
            System.out.println(emailSent);
            return new OtpResponse(true, "OTP verified successfully", true);
        } else{
            return new OtpResponse(false, "Invalid OTP", false);
        }
    }
    //TODO IN CLIENT
    @PostMapping("/check-otp")
    public OtpResponse getOtp(@RequestBody OtpRequest otpRequest) {
        System.out.println(otpRequest);
        String email = otpRequest.getEmail();
        String otp = otpRequest.getOtp();

        // Use the database, NOT unverifiedUsers
        UserEntity user = this.persist.getUserByEmail(email);

        if (user == null) {
            return new OtpResponse(false, "User not found", false);
        }

        if (user.getOtp() != null && user.getOtp().equals(otp)) {
            user.setOtp(null);
            this.persist.update(user);
            return new OtpResponse(true, "OTP verified successfully", true);
        } else {
            return new OtpResponse(false, "Invalid OTP", false);
        }
    }

}
