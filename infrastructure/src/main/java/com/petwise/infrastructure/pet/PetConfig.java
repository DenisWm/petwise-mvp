package com.petwise.infrastructure.pet;

import com.petwise.application.pet.create.CreatePetUseCase;
import com.petwise.application.pet.create.DefaultCreatePetUseCase;
import com.petwise.application.pet.delete.DefaultDeletePetUseCase;
import com.petwise.application.pet.delete.DeletePetUseCase;
import com.petwise.application.pet.retrieve.getbyid.DefaultGetPetByIdUseCase;
import com.petwise.application.pet.retrieve.getbyid.GetPetByIdUseCase;
import com.petwise.application.pet.retrieve.list.DefaultListPetsUseCase;
import com.petwise.application.pet.retrieve.list.ListPetsUseCase;
import com.petwise.application.pet.update.DefaultUpdatePetUseCase;
import com.petwise.application.pet.update.UpdatePetUseCase;
import com.petwise.domain.pet.PetGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Spring configuration for the Pet module. Wires together use cases, gateways, and controllers. */
@Configuration
@SuppressWarnings("PMD.LongVariable")
public class PetConfig {
    public PetConfig() {}

    /**
     * Creates the {@link CreatePetUseCase} bean.
     *
     * @param gateway the pet persistence gateway
     * @return the use case implementation
     */
    @Bean
    public CreatePetUseCase createPetUseCase(final PetGateway gateway) {
        return new DefaultCreatePetUseCase(gateway);
    }

    /**
     * Creates the {@link GetPetByIdUseCase} bean.
     *
     * @param gateway the pet persistence gateway
     * @return the use case implementation
     */
    @Bean
    public GetPetByIdUseCase getPetByIdUseCase(final PetGateway gateway) {
        return new DefaultGetPetByIdUseCase(gateway);
    }

    /**
     * Creates the {@link ListPetsUseCase} bean.
     *
     * @param gateway the pet persistence gateway
     * @return the use case implementation
     */
    @Bean
    public ListPetsUseCase listPetsUseCase(final PetGateway gateway) {
        return new DefaultListPetsUseCase(gateway);
    }

    /**
     * Creates the {@link UpdatePetUseCase} bean.
     *
     * @param gateway the pet persistence gateway
     * @return the use case implementation
     */
    @Bean
    public UpdatePetUseCase updatePetUseCase(final PetGateway gateway) {
        return new DefaultUpdatePetUseCase(gateway);
    }

    /**
     * Creates the {@link DeletePetUseCase} bean.
     *
     * @param gateway the pet persistence gateway
     * @return the use case implementation
     */
    @Bean
    public DeletePetUseCase deletePetUseCase(final PetGateway gateway) {
        return new DefaultDeletePetUseCase(gateway);
    }
}
