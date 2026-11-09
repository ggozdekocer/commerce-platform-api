package tr.com.java.commerceplatform.address.service;

import tr.com.java.commerceplatform.address.entity.Address;
import tr.com.java.commerceplatform.common.exception.ResourceNotFoundException;
import tr.com.java.commerceplatform.user.entity.User;

import java.util.List;

/** Business operations for the delivery addresses of a user. */
public interface AddressService {

    /**
     * Returns all addresses of a user.
     *
     * @param user the owner of the addresses
     * @return the addresses of the user, empty when there are none
     */
    List<Address> getAllByUser(User user);

    /**
     * Saves a new address for a user.
     *
     * @param user    the owner of the address
     * @param address the address to save
     * @return the saved address
     */
    Address save(User user, Address address);

    /**
     * Deletes an address of a user.
     *
     * @param user the user who owns the address
     * @param id   the id of the address to delete
     * @throws ResourceNotFoundException if the address does not exist or belongs to another user
     */
    void delete(User user, Long id);
}