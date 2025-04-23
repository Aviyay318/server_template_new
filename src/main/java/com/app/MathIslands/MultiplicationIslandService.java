package com.app.MathIslands;

import com.app.Math.BaseMath;
import com.app.Math.multiplicationTableService;
import org.springframework.stereotype.Service;

@Service
public class MultiplicationIslandService extends BaseIslandService {

    @Override
    protected BaseMath createService(int level, double success, int questionType) {
        multiplicationTableService service = new multiplicationTableService();
        service.shortAddAndSubtract(level, success, questionType);
        return service;
    }
}
