package com.slay.user.dto.request.course;

import lombok.Data;

import java.io.Serializable;

@Data
public class TrainingCourseMinimaInfo implements Serializable {
    private int id;
    private String name;
}
