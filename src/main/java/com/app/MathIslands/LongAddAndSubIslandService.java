package com.app.MathIslands;

import com.app.Math.AddSubtractService;
import com.app.Math.BaseMath;
import org.springframework.stereotype.Service;

@Service
public class LongAddAndSubIslandService extends BaseIslandService {

    @Override
    protected BaseMath createService(int level, double success, int questionType) {
        AddSubtractService service = new AddSubtractService();
        service.longAddAndSubtract(level, success, questionType);
        return service;
    }
}
