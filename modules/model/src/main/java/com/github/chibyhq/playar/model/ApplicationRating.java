package com.github.chibyhq.playar.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class ApplicationRating {
	@Id UUID uuid;
	Application ratedApplication;
	User ratedBy;
	ScoreEnum score;
}
