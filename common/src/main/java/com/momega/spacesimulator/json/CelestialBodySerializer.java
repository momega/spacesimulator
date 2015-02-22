package com.momega.spacesimulator.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.SurfacePoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by martin on 1/10/15.
 */
@Component
public class CelestialBodySerializer extends AbstractSerializer<CelestialBody> {

    private static final Logger logger = LoggerFactory.getLogger(CelestialBodySerializer.class);

    protected CelestialBodySerializer() {
        super(CelestialBody.class);
    }

    @Override
    public void write(JsonObject object, CelestialBody value, Gson gson) {
        logger.debug("name = {}, surface points = {}", value.getName(), value.getSurfacePoints().size());
    }

    @Override
    public void read(JsonObject object, CelestialBody value, Gson gson) {
        for(SurfacePoint surfacePoint : value.getSurfacePoints()) {
            surfacePoint.setCelestialBody(value);
        }
    }

    @Override
    public Class<?> getClass(JsonObject object) {
        return null;
    }
}
