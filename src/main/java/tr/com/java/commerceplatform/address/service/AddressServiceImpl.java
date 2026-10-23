package tr.com.java.commerceplatform.address.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.com.java.commerceplatform.address.entity.Address;
import tr.com.java.commerceplatform.address.repository.AddressRepository;
import tr.com.java.commerceplatform.common.exception.ResourceNotFoundException;
import tr.com.java.commerceplatform.user.entity.User;

import java.util.List;

/** Default implementation of {@link AddressService} backed by the address repository. */
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    /** {@inheritDoc} */
    @Override
    public List<Address> getAllByUser(User user) {
        return addressRepository.findByUserId(user.getId());
    }

    /** {@inheritDoc} */
    @Override
    public Address save(User user, Address address) {
        address.setUser(user);
        return addressRepository.save(address);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if the address does not exist or belongs to another user
     */
    @Override
    public void delete(User user, Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
        if (!address.getUser().getId().equals(user.getId())) {
            // Same message as a missing address, so other users' ids are not revealed
            throw new ResourceNotFoundException("Address not found with id: " + id);
        }
        addressRepository.delete(address);
    }
}