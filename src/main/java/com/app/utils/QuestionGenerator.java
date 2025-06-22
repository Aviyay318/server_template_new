package com.app.utils;

import com.app.entities.ChildrenNameEntity;
import com.app.entities.ObjectsEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
public class QuestionGenerator {
    public static Map<String, Object> literalProblem(List<ObjectsEntity> objects, List<ChildrenNameEntity> childrenList, int level) {
        Random random = new Random();
        int max = 10;


        ChildrenNameEntity name1 = childrenList.get(random.nextInt(childrenList.size()));
        ChildrenNameEntity name2 = childrenList.get(random.nextInt(childrenList.size()));

        ObjectsEntity obj1 = objects.get(random.nextInt(objects.size()));
        ObjectsEntity obj2 = objects.get(random.nextInt(objects.size()));

        int num1 = random.nextInt(1, max);
        int num2 = random.nextInt(1, max);

        String template = "ל{שם1} יש {מספר1} {עצם1} ו{שם2} {זכר/נקבה1} {זכר/נקבה2} עוד {מספר2} {עצם2} כמה {עצם1} {עצם2} יש ל{שם1} בסך הכל?";

        template = template.replace("{שם1}", name1.getName());
        template = template.replace("{שם2}", name2.getName());
        template = template.replace("{מספר1}", String.valueOf(num1));
        template = template.replace("{מספר2}", String.valueOf(num2));

        template = template.replace("{זכר/נקבה1}", name2.getGender().equals("male") ? "נתן" : "נתנה");
        template = template.replace("{זכר/נקבה2}", name1.getGender().equals("male") ? "לו" : "לה");

        String nameObj1 = (num1 > 1) ? obj1.getPluralName() : obj1.getSingularName();
        String nameObj2 = (num2 > 1) ? obj2.getPluralName() : obj2.getSingularName();
        template = template.replace("{עצם1}", nameObj1);
        template = template.replace("{עצם2}", nameObj2);

        List<Map<String, Object>> imageHint = new ArrayList<>();
        imageHint.add(Map.of(
                "name", nameObj1,
                "count", num1,
                "svg", obj1.getSvg()
        ));
        imageHint.add(Map.of(
                "name", nameObj2,
                "count", num2,
                "svg", obj2.getSvg()
        ));

        Map<String, Object> result = new HashMap<>();
        result.put("question", template);
        result.put("hint", num1 + " + " + num2);
        result.put("answer", num1 + num2);
        result.put("svg1", obj1.getSvg());
        result.put("svg2", obj2.getSvg());
        result.put("imageHint", imageHint);

        return result;
    }

}
