package com.slay.user.dto.response.course;

import com.slay.user.dto.response.user.UserLiteDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TrainingCourseLiteDTO implements Serializable {
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

}
