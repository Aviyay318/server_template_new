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
import java.util.HashMap;

@RestController
public class GeneralController {
    private HashMap<String,UserEntity> unverifiedUsers = new HashMap<>();

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
    System.out.println(email);
    System.out.println(user);;
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
        System.out.println("Received User " + user);
        System.out.println("1");
        boolean success = false;
        System.out.println("2");
        Integer errorCode = Constants.EMAIL_EXIST;
        System.out.println("3");
        String otp = null;
        System.out.println("4");

        if (user != null && persist.getUserByEmail(user.getEmail()) == null) {
            System.out.println("5");
            otp = GeneralUtils.generateOtp();
            System.out.println("6");
            user.setOtp(otp);
            System.out.println("7: " + user.getOtp());
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                System.out.println("8");
                System.out.println("Password is: " + user.getPassword());
            } else{
                System.out.println("9");
                String hashed = GeneralUtils.hashMd5(user.getEmail(), user.getPassword());
                System.out.println("10 : " + hashed);
                user.setPassword(hashed);
                System.out.println("11 : " + user.getPassword());
                unverifiedUsers.put(user.getEmail(),user);
                System.out.println("12 : unverified user " + unverifiedUsers.get(user.getEmail()));
                System.out.println("13 : unverified user email " + unverifiedUsers.get(user.getEmail()).getEmail());
                System.out.println("13 x2 : supposedly user email " + user.getEmail());

                boolean emailSent = ApiEmailProcessor.sendEmail(user.getEmail(), "OTP Verification", "Here is your code: " + otp);
                System.out.println("14 : " + emailSent);

                System.out.println("OTP sent: " + emailSent);
                success = true;
                errorCode=null;
            }
        } else{
            System.out.println("something went wrong, or email already exist");
        }
        return new RegisterResponse(success,errorCode,otp);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public LoginResponse login(@RequestBody LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        boolean success = false;
        String token = null;
        boolean isAdmin = false;
        String hashed = GeneralUtils.hashMd5(email, password);
        System.out.println(hashed);
        UserEntity user = this.persist.getUserByEmailAndPassword(email);

        if (user!=null){
              if (user.getPassword().equals(hashed)||Constants.ADMIN_EMAIL.equals(email)&&Constants.ADMIN_PASSWORD.equals(password)) {
                success = true;
                token = hashed;
             if (Constants.ADMIN_EMAIL.equals(email)){
                 isAdmin = true;
             }
            }

        }
      return new LoginResponse(success,token,isAdmin);
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
            return new OtpResponse(true, "OTP verified successfully", true);
        } else{
            return new OtpResponse(false, "Invalid OTP", false);
        }
    }
    //TODO IN CLIENT
    @PostMapping ("/check-otp")
    public OtpResponse getOtp(@RequestBody OtpRequest otpRequest) {
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
            return new OtpResponse(true, "OTP verified successfully", true);
        } else{
            return new OtpResponse(false, "Invalid OTP", false);
        }
    }
}
