package tr.com.java.commerceplatform.address.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import tr.com.java.commerceplatform.address.dto.AddressRequest;
import tr.com.java.commerceplatform.address.dto.AddressResponse;
import tr.com.java.commerceplatform.address.entity.Address;
import tr.com.java.commerceplatform.address.service.AddressService;
import tr.com.java.commerceplatform.common.response.CommonResponse;
import tr.com.java.commerceplatform.user.entity.User;

import java.util.List;

/** REST endpoints for managing the delivery addresses of the logged-in user. */
@RestController
@RequestMapping("/user/address")
@RequiredArgsConstructor
@Tag(name = "Addresses", description = "Manage the delivery addresses of the logged-in user")
public class AddressController {

    private final AddressService addressService;

    /**
     * Returns all addresses of the logged-in user.
     *
     * @param authentication the authentication of the current user, filled in by Spring Security
     * @return HTTP 200 with the address list wrapped in a CommonResponse
     */
    @Operation(
            summary = "List my addresses",
            description = "Returns all saved delivery addresses of the logged-in user. Requires a valid token.")
    @ApiResponse(responseCode = "200", description = "Addresses returned")
    @ApiResponse(responseCode = "403", description = "The token is missing, invalid or expired")
    @GetMapping
    public ResponseEntity<CommonResponse<List<AddressResponse>>> getAddresses(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<AddressResponse> addresses = addressService.getAllByUser(user).stream()
                .map(AddressResponse::from)
                .toList();
        return ResponseEntity.ok(CommonResponse.success(addresses, "Addresses retrieved successfully"));
    }

    /**
     * Adds a new address for the logged-in user.
     *
     * @param request        the address data to save
     * @param authentication the authentication of the current user, filled in by Spring Security
     * @return HTTP 201 with the saved address wrapped in a CommonResponse
     */
    @Operation(
            summary = "Add an address",
            description = "Saves a new delivery address for the logged-in user. Requires a valid token.")
    @ApiResponse(responseCode = "201", description = "Address saved")
    @ApiResponse(responseCode = "400", description = "The request body is missing or malformed")
    @ApiResponse(responseCode = "403", description = "The token is missing, invalid or expired")
    @PostMapping
    public ResponseEntity<CommonResponse<AddressResponse>> addAddress(@RequestBody AddressRequest request,
                                                                      Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Address saved = addressService.save(user, request.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.success(AddressResponse.from(saved),
                        "Address created successfully", HttpStatus.CREATED));
    }

    /**
     * Deletes an address of the logged-in user.
     *
     * @param id             the id of the address to delete
     * @param authentication the authentication of the current user, filled in by Spring Security
     * @return HTTP 200 with an empty payload and a confirmation message
     */
    @Operation(
            summary = "Delete an address",
            description = "Deletes one of the logged-in user's addresses. Requires a valid token.")
    @ApiResponse(responseCode = "200", description = "Address deleted")
    @ApiResponse(responseCode = "403", description = "The token is missing, invalid or expired")
    @ApiResponse(responseCode = "404", description = "The address does not exist or belongs to another user")
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> deleteAddress(@PathVariable Long id,
                                                              Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        addressService.delete(user, id);
        return ResponseEntity.ok(CommonResponse.success(null, "Address deleted successfully"));
    }
}