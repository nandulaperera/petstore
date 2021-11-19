package com.example.petstore;

import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.List;

@Path("/v1/petTypes")
@Produces("application/json")
public class PetTypeResource {

	public List<PetType> petTypes = new ArrayList<>(
			List.of(
					setPetTypeData(1,"Dog"),
					setPetTypeData(2,"Cat"),
					setPetTypeData(3,"Bird")
			)
	);

	private PetType setPetTypeData(int petTypeId, String type){
		PetType petType = new PetType();
		petType.setPetTypeId(petTypeId);
		petType.setPetType(type);
		return petType;
	}

	@APIResponses(value = {
			@APIResponse(responseCode = "200", description = "All Pet Types", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(ref = "PetType"))) })
	@GET
	public Response getPetTypes() {
		return Response.ok(petTypes).build();
	}

	@APIResponses(value = {
			@APIResponse(responseCode = "200", description = "Pet Type for id", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(ref = "PetType"))),
			@APIResponse(responseCode = "404", description = "No Pet Type found for the id.") })
	@GET
	@Path("{petTypeId}")
	public Response getPetType(@PathParam("petTypeId") int petTypeId) {
		if (petTypeId < 0) {
			return Response.status(Status.NOT_FOUND).build();
		}
		PetType foundPetType = null;
		for(PetType petType : petTypes){
			if(petType.getPetTypeId() == petTypeId){
				foundPetType = petType;
				break;
			}
		}
		if(foundPetType != null){
			return Response.ok(foundPetType).build();
		}else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@APIResponses(value = {
			@APIResponse(responseCode = "200", description = "Add a Pet type", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(ref = "PetType"))),
			@APIResponse(responseCode = "500", description = "A pet type with the same id already exists.")
	})
	@POST
	@RequestBody
	public Response addPetType(@RequestBody PetType newPetType) {
		boolean petTypeExists = false;
		for(PetType petType : petTypes){
			if(petType.getPetTypeId() == newPetType.getPetTypeId()){
				petTypeExists = true;
				break;
			}
		}
		if(petTypeExists){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		petTypes.add(setPetTypeData(newPetType.getPetTypeId(),newPetType.getPetType()));
		return Response.ok(newPetType).build();
	}

	@APIResponses(value = {
			@APIResponse(responseCode = "200", description = "Update pet type details", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(ref = "PetType"))),
			@APIResponse(responseCode = "500", description = "The pet type does not exist.")
	})

	@PUT
	@RequestBody
	public Response updatePetTypeDetails(@RequestBody PetType updatedPetTypeDetails) {
		for(int i = 0; i < petTypes.size(); i++){
			PetType petType = petTypes.get(i);
			if(petType.getPetTypeId() == updatedPetTypeDetails.getPetTypeId()){
				petTypes.set(i,updatedPetTypeDetails);
				return Response.ok(updatedPetTypeDetails).build();
			}
		}
		return Response.status(Status.INTERNAL_SERVER_ERROR).build();

	}

	@APIResponses(value = {
			@APIResponse(responseCode = "200", description = "Delete pet type details", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(ref = "PetType"))),
			@APIResponse(responseCode = "500", description = "The pet type does not exist.")
	})

	@DELETE
	@Path("{petTypeId}")
	public Response deletePetDetails(@PathParam("petTypeId") int petTypeId) {
		for(int i = 0; i < petTypes.size(); i++){
			PetType petType = petTypes.get(i);
			if(petType.getPetTypeId() == petTypeId){
				petTypes.remove(petType);
				return Response.ok(petType).build();
			}
		}
		return Response.status(Status.INTERNAL_SERVER_ERROR).build();

	}

}
