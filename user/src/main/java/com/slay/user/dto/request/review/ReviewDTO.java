package com.slay.user.dto.request.review;

import com.slay.user.dto.request.course.TrainingCourseMinimaInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ReviewDTO implements Serializable {
    private int id;
    private String title;
    private String text;
    private int rating;
    //private UserDTO author;
    private TrainingCourseMinimaInfo course;
    private List<String> images;

}