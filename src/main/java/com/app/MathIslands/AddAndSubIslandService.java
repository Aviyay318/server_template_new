package com.app.MathIslands;

import com.app.Math.AddSubtractService;
import com.app.Math.BaseMath;
import org.springframework.stereotype.Service;

@Service
public class AddAndSubIslandService extends BaseIslandService {

    public static final int NUMBER_OF_MULTIPLE_QUESTION = 16;

    @Override
    protected BaseMath createService(int level, double success, int questionType) {
        AddSubtractService service = new AddSubtractService();
        service.shortAddAndSubtract(level, success, questionType);
        return service;
    }
}
