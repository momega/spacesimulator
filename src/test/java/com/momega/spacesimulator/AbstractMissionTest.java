package com.momega.spacesimulator;

import com.momega.spacesimulator.builder.AbstractModelBuilder;
import com.momega.spacesimulator.context.AppConfig;
import com.momega.spacesimulator.context.DefaultApplication;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.HistoryPoint;
import com.momega.spacesimulator.model.HistoryPointOrigin;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.service.HistoryPointListener;
import com.momega.spacesimulator.service.HistoryPointService;
import com.momega.spacesimulator.service.ModelService;

/**
 * Created by martin on 1/11/15.
 */
public abstract class AbstractMissionTest {

    protected DefaultApplication application;

    private Timestamp startTime;

    protected ModelService modelService;

    protected HistoryPointService historyPointService;

    protected void setup(Class<? extends AbstractModelBuilder> modelBuilderClass) {
        application = new DefaultApplication(AppConfig.class);
        application.init(modelBuilderClass);
        startTime = ModelHolder.getModel().getTime();
        modelService = application.getService(ModelService.class);
        historyPointService = application.getService(HistoryPointService.class);
        historyPointService.addHistoryPointListener(new HistoryPointListener() {
            @Override
            public void historyPointCreated(HistoryPoint historyPoint) {
                if (HistoryPointOrigin.END.equals(historyPoint.getOrigin())) {
                    modelService.removeMovingObject(historyPoint.getSpacecraft());
                }
            }
        });
    }

    protected void runTo(int seconds) {
        Timestamp current = ModelHolder.getModel().getTime();
        Timestamp requested = startTime.add(seconds);
        int steps = (int) requested.subtract(current);
        for(int i=0; i<steps; i++) {
            application.next(true, 1.0);
        }
        application.next(false, 1.0);
    }
}
