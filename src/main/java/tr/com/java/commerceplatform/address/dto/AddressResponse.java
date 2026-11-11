package tr.com.java.commerceplatform.address.dto;

import tr.com.java.commerceplatform.address.entity.Address;

/** Data returned to the client for a delivery address. */
public record AddressResponse(
        Long id,
        String title,
        String name,
        String surname,
        String phone,
        String city,
        String district,
        String neighborhood) {

    /** Builds a response from an Address entity, leaving out the user. */
    public static AddressResponse from(Address address) {
        return new AddressResponse(
                address.getId(),
                address.getTitle(),
                address.getName(),
                address.getSurname(),
                address.getPhone(),
                address.getCity(),
                address.getDistrict(),
                address.getNeighborhood());
    }
}