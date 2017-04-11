package com.github.chiby.player.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Gadget {

	@Id UUID uuid;
	IGadgetDefinition definition;
}