package com.example.petstore;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "PetType")
public class PetType {

	@Schema(required = true, description = "Pet Type Id")
	@JsonProperty("petType_id")
	private Integer petTypeId;

	@Schema(required = true, description = "Pet type")
	@JsonProperty("pet_type")
	private String petType;

	public Integer getPetTypeId() {
		return petTypeId;
	}

	public void setPetTypeId(Integer petTypeId) {
		this.petTypeId = petTypeId;
	}

	public String getPetType() {
		return petType;
	}

	public void setPetType(String petType) {
		this.petType = petType;
	}

	@Override
	public String toString() {
		return "PetType{" +
				"petTypeId=" + petTypeId +
				", petType='" + petType + '\'' +
				'}';
	}
}
