package tr.com.java.commerceplatform.address.controller;

import tr.com.java.commerceplatform.address.entity.Address;
import tr.com.java.commerceplatform.user.entity.User;
import tr.com.java.commerceplatform.address.service.IAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/address")
@RequiredArgsConstructor
public class AddressController {

    private final IAddressService addressService;

    @GetMapping
    public List<Address> getAddresses(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return addressService.getAllByUser(user);
    }

    @PostMapping
    public Address addAddress(@RequestBody Address address, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return addressService.save(user, address);
    }

    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        addressService.delete(user, id);
    }
}