package tr.com.java.commerceplatform.user.service;

import tr.com.java.commerceplatform.user.entity.Role;

import java.util.List;

/** Business operations for reading user roles. */
public interface RoleService {

    /**
     * Returns all roles a user can sign up with.
     *
     * @return the list of all roles, empty when there are none
     */
    List<Role> getAll();
}