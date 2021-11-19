package com.example.petstore;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

@Path("/v1/pets")
@Produces("application/json")
public class PetResource {

	public List<Pet> pets = new ArrayList<>(
			List.of(
					setPetData(1,3,"Boola","Dog"),
					setPetData(2,4,"Sudda","Cat"),
					setPetData(3,2,"Peththappu","Bird")
			)
	);

	private Pet setPetData(int petId, int petAge, String petName, String petType){
		Pet pet = new Pet();
		pet.setPetId(petId);
		pet.setPetAge(petAge);
		pet.setPetName(petName);
		pet.setPetType(petType);
		return pet;
	}

	@APIResponses(value = {
			@APIResponse(responseCode = "200", description = "All Pets", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(ref = "Pet"))) })
	@GET
	public Response getPets() {
		return Response.ok(pets).build();
	}

	@APIResponses(value = {
			@APIResponse(responseCode = "200", description = "Pet for id", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(ref = "Pet"))),
			@APIResponse(responseCode = "404", description = "No Pet found for the id.") })
	@GET
	@Path("{petId}")
	public Response getPet(@PathParam("petId") int petId) {
		if (petId < 0) {
			return Response.status(Status.NOT_FOUND).build();
		}
		Pet foundPet = null;
		for(Pet pet : pets){
			if(pet.getPetId() == petId){
				foundPet = pet;
				break;
			}
		}
		if(foundPet != null){
			return Response.ok(foundPet).build();
		}else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@APIResponses(value = {
			@APIResponse(responseCode = "200", description = "Add a Pet", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(ref = "Pet"))),
			@APIResponse(responseCode = "500", description = "A pet with the same id already exists.")
	})
	@POST
	@RequestBody
	public Response addPetDetails(@RequestBody Pet newPet) {
		boolean petExists = false;
		for(Pet pet : pets){
			if(pet.getPetId() == newPet.getPetId()){
				petExists = true;
				break;
			}
		}
		if(petExists){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		pets.add(setPetData(newPet.getPetId(),newPet.getPetAge(),newPet.getPetName(), newPet.getPetType()));
		return Response.ok(newPet).build();
	}

	@APIResponses(value = {
			@APIResponse(responseCode = "200", description = "Update pet details", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(ref = "Pet"))),
			@APIResponse(responseCode = "500", description = "The pet does not exist.")
	})

	@PUT
	@RequestBody
	public Response updatePetDetails(@RequestBody Pet updatedPetDetails) {
		for(int i = 0; i < pets.size(); i++){
			Pet pet = pets.get(i);
			if(pet.getPetId() == updatedPetDetails.getPetId()){
				pets.set(i,updatedPetDetails);
				return Response.ok(updatedPetDetails).build();
			}
		}
		return Response.status(Status.INTERNAL_SERVER_ERROR).build();

	}

	@APIResponses(value = {
			@APIResponse(responseCode = "200", description = "Delete pet details", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(ref = "Pet"))),
			@APIResponse(responseCode = "500", description = "The pet does not exist.")
	})

	@DELETE
	@Path("{petId}")
	public Response deletePetDetails(@PathParam("petId") int petId) {
		for(int i = 0; i < pets.size(); i++){
			Pet pet = pets.get(i);
			if(pet.getPetId() == petId){
				pets.remove(pet);
				return Response.ok(pet).build();
			}
		}
		return Response.status(Status.INTERNAL_SERVER_ERROR).build();

	}


	@APIResponses(value = {
			@APIResponse(responseCode = "200", description = "Get pet details by name", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(ref = "Pet"))),
			@APIResponse(responseCode = "404", description = "No Pet found for the name.")
	})

	@GET
	@Path("query/byName")
	public Response getPetByName(@QueryParam("petName") String petName) {
		if (petName.equals("")) {
			return Response.status(Status.NOT_FOUND).build();
		}
		List<Pet> foundPets = new ArrayList<>();
		for(Pet pet : pets){
			if(pet.getPetName().equals(petName)){
				foundPets.add(pet);
			}
		}
		if(foundPets.size() > 0){
			return Response.ok(foundPets).build();
		}else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@APIResponses(value = {
			@APIResponse(responseCode = "200", description = "Get pet details by name", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(ref = "Pet"))),
			@APIResponse(responseCode = "404", description = "No Pet found for the name.")
	})

	@GET
	@Path("query/byAge")
	public Response getPetByAge(@QueryParam("petAge") int petAge) {
		if (petAge < 0) {
			return Response.status(Status.NOT_FOUND).build();
		}
		List<Pet> foundPets = new ArrayList<>();
		for(Pet pet : pets){
			if(pet.getPetAge() == petAge){
				foundPets.add(pet);
			}
		}
		if(foundPets.size() > 0){
			return Response.ok(foundPets).build();
		}else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@APIResponses(value = {
			@APIResponse(responseCode = "200", description = "Get pet details by name", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(ref = "Pet"))),
			@APIResponse(responseCode = "404", description = "No Pet found for the name.")
	})

	@GET
	@Path("query/byType")
	public Response getPetByType(@QueryParam("petType") String petType) {
		if (petType.equals("")) {
			return Response.status(Status.NOT_FOUND).build();
		}
		List<Pet> foundPets = new ArrayList<>();
		for(Pet pet : pets){
			if(pet.getPetType().equals(petType)){
				foundPets.add(pet);
			}
		}
		if(foundPets.size() > 0){
			return Response.ok(foundPets).build();
		}else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}
}
