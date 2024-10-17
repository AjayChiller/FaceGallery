package com.example.facegallery.data;

import com.google.mediapipe.tasks.components.containers.Detection;
import com.google.mediapipe.tasks.vision.facedetector.FaceDetectorResult;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

class FaceDetectorResultV2 extends FaceDetectorResult implements Serializable {
    public FaceDetectorResultV2() {
        super();
    }

    @Override
    public long timestampMs() {

        return 0;
    }

    @Override
    public List<Detection> detections() {
        return Collections.emptyList();
    }
}
