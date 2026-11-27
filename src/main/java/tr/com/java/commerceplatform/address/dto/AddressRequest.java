package tr.com.java.commerceplatform.address.dto;

import tr.com.java.commerceplatform.address.entity.Address;

/** Data a client sends when saving a delivery address. */
public record AddressRequest(
        String title,
        String name,
        String surname,
        String phone,
        String city,
        String district,
        String neighborhood) {

    /** Converts this request into a new Address entity (without id and user). */
    public Address toEntity() {
        Address address = new Address();
        address.setTitle(title);
        address.setName(name);
        address.setSurname(surname);
        address.setPhone(phone);
        address.setCity(city);
        address.setDistrict(district);
        address.setNeighborhood(neighborhood);
        return address;
    }
}