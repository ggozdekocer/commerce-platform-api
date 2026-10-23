package tr.com.java.commerceplatform.address.service;

import tr.com.java.commerceplatform.address.entity.Address;
import tr.com.java.commerceplatform.user.entity.User;
import java.util.List;

public interface AddressService {
    List<Address> getAllByUser(User user);
    Address save(User user, Address address);
    void delete(User user, Long id);
}