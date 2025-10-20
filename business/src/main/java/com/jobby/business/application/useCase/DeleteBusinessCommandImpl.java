package com.jobby.business.application.useCase;

import com.jobby.business.domain.ports.in.DeleteBusinessCommand;
import com.jobby.business.domain.ports.out.repositories.WriteOnlyBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import org.springframework.stereotype.Component;

@Component
public class DeleteBusinessCommandImpl implements DeleteBusinessCommand {
    private final WriteOnlyBusinessRepository businessRepository;

    public DeleteBusinessCommandImpl(WriteOnlyBusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    @Override
    public Result<Void, Error> delete(int id) {
        return this.businessRepository.findById(id)
                .flatMap(this.businessRepository::delete);
    }
}
