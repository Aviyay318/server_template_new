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
        service.setMaxRange(10);
        service.setMinRange(level < 11 ? level : 1);
        return service;
    }
}
