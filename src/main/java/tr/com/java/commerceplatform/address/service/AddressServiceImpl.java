package tr.com.java.commerceplatform.address.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tr.com.java.commerceplatform.address.entity.Address;
import tr.com.java.commerceplatform.address.repository.AddressRepository;
import tr.com.java.commerceplatform.common.exception.ResourceNotFoundException;
import tr.com.java.commerceplatform.user.entity.User;

import java.util.List;

/** Default implementation of {@link AddressService} backed by the address repository. */
@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    /** {@inheritDoc} */
    @Override
    public List<Address> getAllByUser(User user) {
        log.debug("Loading addresses of user id={}", user.getId());
        return addressRepository.findByUserId(user.getId());
    }

    /** {@inheritDoc} */
    @Override
    public Address save(User user, Address address) {
        address.setUser(user);
        Address saved = addressRepository.save(address);
        log.info("Address saved for user id={}", user.getId());
        return saved;
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if the address does not exist or belongs to another user
     */
    @Override
    public void delete(User user, Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Address delete failed: address id={} not found", id);
                    return new ResourceNotFoundException("Address not found with id: " + id);
                });
        if (!address.getUser().getId().equals(user.getId())) {
            log.warn("User id={} tried to delete address id={} of another user", user.getId(), id);
            // Same message as a missing address, so other users' ids are not revealed
            throw new ResourceNotFoundException("Address not found with id: " + id);
        }
        addressRepository.delete(address);
        log.info("Address id={} deleted by user id={}", id, user.getId());
    }
}