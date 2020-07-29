package edu.codegym.toam.service.contract;

import edu.codegym.toam.exception.ContractException;
import edu.codegym.toam.model.Contracts;

import java.util.Date;

public interface IContractService {
    Iterable<Contracts> findAll();

    Contracts findById(Long id);

    Contracts update(Contracts contracts);

    void removeById(Long id);

    Contracts create(Contracts contracts);

    Iterable<Contracts> findAllContractsByHostId(Long id);

    Iterable<Contracts> findAllContractsByRenterId(Long id);

    Iterable<Contracts> findAllContractsByRenterIdAndPropertyId(Long renterId,Long propertyId);

    boolean checkCheckinTimeAndCurrentTime (Date checkinTime, Date currentTime) throws ContractException;

}
