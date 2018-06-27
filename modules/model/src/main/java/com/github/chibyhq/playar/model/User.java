package com.github.chibyhq.playar.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import com.querydsl.core.annotations.QueryEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@QueryEntity
@Entity
public class User {
   @Id 
   @GeneratedValue(generator = "uuid2")
   @GenericGenerator(name = "uuid2", strategy = "uuid2")
   @Column(columnDefinition = "BINARY(16)")
   public UUID uuid;
   public String nickname;
   /**
    * URL pointing to the user's avatar
    */
   public String avatar;
}
