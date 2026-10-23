package tr.com.java.commerceplatform.address.service;

import tr.com.java.commerceplatform.address.entity.Address;
import tr.com.java.commerceplatform.user.entity.User;
import tr.com.java.commerceplatform.address.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    public List<Address> getAllByUser(User user) {
        return addressRepository.findByUserId(user.getId());
    }

    @Override
    public Address save(User user, Address address) {
        address.setUser(user);
        return addressRepository.save(address);
    }

    @Override
    public void delete(User user, Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        if (!address.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        addressRepository.delete(address);
    }
}