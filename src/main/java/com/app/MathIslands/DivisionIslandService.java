package com.app.MathIslands;

import com.app.Math.BaseMath;
import com.app.Math.multiplicationTableService;
import org.springframework.stereotype.Service;

@Service
public class DivisionIslandService extends BaseIslandService {

    @Override
    protected BaseMath createService(int level, double success, int questionType) {
        multiplicationTableService service = new multiplicationTableService();
        service.setOperator("/");

        int adjustedLevel = level;

        if (success >= 0.85 && level < 10) {
            adjustedLevel++;
        } else if (success < 0.5 && level > 1) {
            adjustedLevel--;
        }

        int min = Math.max(1, adjustedLevel);
        int max = Math.min(100, adjustedLevel * 4);

        if (max - min < 4) {
            max = min + 4;
        }

        service.setMinRange(min);
        service.setMaxRange(max);


        return service;
    }
    }
