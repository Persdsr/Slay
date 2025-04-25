package com.slay.user.dto.request.course;

import com.slay.user.dto.request.review.ReviewDTO;
import com.slay.user.dto.response.user.UserLiteDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Data
public class TrainingCourseInfoDTO implements Serializable {
    private int id;
    private String name;
    private String description;
    private String poster;
    private int price;
    private Date createAt;
    private UserLiteDTO author;
    private String category;
    private Long courseBuyers;
    private Long reviewsCount;
    private Set<ReviewDTO> reviews;

}
