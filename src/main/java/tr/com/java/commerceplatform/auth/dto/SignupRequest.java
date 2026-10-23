package tr.com.java.commerceplatform.auth.dto;

/**
 * Request body for the signup endpoint.
 * Store fields are only used when the new user registers as a store owner.
 *
 * @param name             full name of the user
 * @param email            email address, used as the login name
 * @param password         raw password chosen by the user
 * @param roleId           id of the role the user signs up with
 * @param storeName        name of the store (store owners only)
 * @param storePhone       contact phone of the store (store owners only)
 * @param storeTaxNo       tax number of the store (store owners only)
 * @param storeBankAccount bank account of the store (store owners only)
 */
public record SignupRequest(
        String name,
        String email,
        String password,
        Long roleId,
        String storeName,
        String storePhone,
        String storeTaxNo,
        String storeBankAccount
) {
}