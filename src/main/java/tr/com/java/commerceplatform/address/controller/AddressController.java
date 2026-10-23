package tr.com.java.commerceplatform.address.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.java.commerceplatform.address.entity.Address;
import tr.com.java.commerceplatform.address.service.AddressService;
import tr.com.java.commerceplatform.common.response.CommonResponse;
import tr.com.java.commerceplatform.user.entity.User;

import java.util.List;

/** REST endpoints for managing the delivery addresses of the logged-in user. */
@RestController
@RequestMapping("/user/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    /**
     * Returns all addresses of the logged-in user.
     *
     * @param authentication the authentication of the current user, filled in by Spring Security
     * @return HTTP 200 with the address list wrapped in a CommonResponse
     */
    @GetMapping
    public ResponseEntity<CommonResponse<List<Address>>> getAddresses(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Address> addresses = addressService.getAllByUser(user);
        return ResponseEntity.ok(CommonResponse.success(addresses, "Addresses retrieved successfully"));
    }

    /**
     * Adds a new address for the logged-in user.
     *
     * @param address        the address to save
     * @param authentication the authentication of the current user, filled in by Spring Security
     * @return HTTP 201 with the saved address wrapped in a CommonResponse
     */
    @PostMapping
    public ResponseEntity<CommonResponse<Address>> addAddress(@RequestBody Address address,
                                                              Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Address saved = addressService.save(user, address);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.success(saved, "Address created successfully", HttpStatus.CREATED));
    }

    /**
     * Deletes an address of the logged-in user.
     *
     * @param id             the id of the address to delete
     * @param authentication the authentication of the current user, filled in by Spring Security
     * @return HTTP 200 with an empty payload and a confirmation message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> deleteAddress(@PathVariable Long id,
                                                              Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        addressService.delete(user, id);
        return ResponseEntity.ok(CommonResponse.success(null, "Address deleted successfully"));
    }
}