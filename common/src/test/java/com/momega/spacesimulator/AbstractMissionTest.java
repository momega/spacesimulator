package com.momega.spacesimulator;

import com.momega.spacesimulator.builder.AbstractModelBuilder;
import com.momega.spacesimulator.context.AppConfig;
import com.momega.spacesimulator.context.DefaultApplication;
import com.momega.spacesimulator.model.HistoryPoint;
import com.momega.spacesimulator.model.HistoryPointOrigin;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.RunStep;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.service.HistoryPointListener;
import com.momega.spacesimulator.service.HistoryPointService;
import com.momega.spacesimulator.service.ModelBuilderFactory;
import com.momega.spacesimulator.service.ModelService;

/**
 * Created by martin on 1/11/15.
 */
public abstract class AbstractMissionTest {

    protected DefaultApplication application;

    private Timestamp startTime;

    protected ModelService modelService;
    
    protected Model model;

    protected HistoryPointService historyPointService;

    protected void setup(Class<? extends AbstractModelBuilder> modelBuilderClass) {
        application = new DefaultApplication(AppConfig.class);
        model = application.getService(ModelBuilderFactory.class).init(modelBuilderClass);
        startTime = model.getTime();
        modelService = application.getService(ModelService.class);
        historyPointService = application.getService(HistoryPointService.class);
        historyPointService.addHistoryPointListener(new HistoryPointListener() {
            @Override
            public void historyPointCreated(HistoryPoint historyPoint) {
                if (HistoryPointOrigin.END.equals(historyPoint.getOrigin())) {
                    modelService.removeMovingObject(model, historyPoint.getSpacecraft());
                }
            }
            
            @Override
			public boolean supports(HistoryPoint historyPoint) {
				return true;
			}
        });
    }

    protected void runTo(int seconds) {
        Timestamp current = model.getTime();
        Timestamp requested = startTime.add(seconds);
        int steps = (int) requested.subtract(current);
        RunStep runStep = RunStep.create(current, 1, true);
        for(int i=0; i<steps; i++) {
            application.next(model, runStep);
            runStep.next();
        }
        runStep.setRunningHeadless(false);
        application.next(model, runStep);
    }
}
