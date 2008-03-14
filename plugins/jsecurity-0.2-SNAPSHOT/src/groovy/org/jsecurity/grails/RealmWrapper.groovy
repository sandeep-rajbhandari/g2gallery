/*
 * Copyright 2007 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jsecurity.grails

import org.apache.commons.logging.LogFactory
import org.jsecurity.authc.AuthenticationException
import org.jsecurity.authc.AuthenticationToken
import org.jsecurity.authz.AuthorizationException
import org.jsecurity.authz.Permission
import org.jsecurity.authz.UnauthorizedException
import org.jsecurity.realm.Realm
import org.jsecurity.authc.Account
import org.jsecurity.authc.support.SimpleAccount

/**
 * Simple implementation of the JSecurity Realm that wraps a 'Realm'
 * artefact. It is basically an adapter between the Grails world and
 * the JSecurity world.
 * @author Peter Ledbrook
 */
class RealmWrapper implements Realm {
    private static final LOGGER = LogFactory.getLog('grails.app.realm')

    Object realm
    Class tokenClass

    void setRealm(Object realm) {
        this.realm = realm
    }

    void setTokenClass(Class clazz) {
        this.tokenClass = clazz
    }

    Account getAccount(AuthenticationToken token) throws AuthenticationException {
        // If the target realm has an 'authenticate' method, we use
        // that.
        try {
            def user = this.realm.authenticate(token)

            // Return an info object back to the caller. Note that the
            // authenticate() method may return a collection of user
            // identifiers, not just a single username. Of course, this
            // implies that the 'user' variable is misnamed :p
            def principals = []
            if (user instanceof Collection) {
                principals.addAll(user)
            }
            else if (user instanceof String) {
                principals << user
            }

            return new SimpleAccount(principals, null)
        }
        catch (MissingMethodException ex) {
            // No authentication performed.
            if (ex.message.indexOf('authenticate') && LOGGER.errorEnabled) {
                LOGGER.error "Unable to authenticate with ${this.realm.class.name}", ex
            }
            return null
        }
        catch (Exception ex) {
            if (LOGGER.infoEnabled) {
                LOGGER.info "Unable to authenticate with ${this.realm.class.name} - ${ex.message}"
            }
            if (LOGGER.debugEnabled) {
                LOGGER.debug 'The exception', ex
            }
            throw ex
        }
    }

    /* (non-Javadoc)
     * @see org.jsecurity.realm.Realm#getName()
     */
    String getName() {
        return this.realm.class.name
    }

    /* (non-Javadoc)
     * @see org.jsecurity.realm.Realm#supports(java.lang.Class)
     */
    boolean supports(Class clazz) {
        if (this.tokenClass) {
            return this.tokenClass.isAssignableFrom(clazz)
        }
        else {
            return false
        }
    }

    /* (non-Javadoc)
     * @see org.jsecurity.authz.AuthorizationOperations#checkPermission(Object, org.jsecurity.authz.Permission)
     */
    void checkPermission(Object principal, Permission permission) throws AuthorizationException {
        if (!isPermitted(principal, permission)) {
            throw new UnauthorizedException("User '${principal.name}' does not have permission '${permission}'")
        }
    }

    /* (non-Javadoc)
     * @see org.jsecurity.authz.AuthorizationOperations#checkPermissions(Object, java.util.Collection)
     */
    void checkPermissions(Object principal, Collection<Permission> permissions) throws AuthorizationException {
        if (!isPermittedAll(principal, permissions)) {
            throw new UnauthorizedException("User '${principal.name}' does not have the required permissions.")
        }
    }

    /* (non-Javadoc)
     * @see org.jsecurity.authz.AuthorizationOperations#checkRole(Object, java.lang.String)
     */
    void checkRole(Object principal, String role) throws AuthorizationException {
        if (!hasRole(principal, role)) {
            throw new UnauthorizedException("User '${principal.name}' does not have role '${role}'")
        }
    }

    /* (non-Javadoc)
     * @see org.jsecurity.authz.AuthorizationOperations#checkRoles(Object, java.util.Collection)
     */
    void checkRoles(Object principal, Collection<String> roles) throws AuthorizationException {
        if (!hasAllRoles(principal, roles)) {
            throw new UnauthorizedException("User '${principal.name}' does not have all these roles: ${roles}")
        }
    }

    /* (non-Javadoc)
     * @see org.jsecurity.authz.AuthorizationOperations#hasAllRoles(Object, java.util.Collection)
     */
    boolean hasAllRoles(Object principal, Collection<String> roles) {
        // First try the 'hasAllRoles' method on the realm.
        try {
            this.realm.hasAllRoles(principal, roles)
        }
        catch (MissingMethodException ex) {
            // No specific method, so just check each role individually
            // until we find one that the principal does not have, or
            // we reach the end of the collection of roles.
            roles.each { role ->
                // It the principal does not have this role, then
                // we can immediately return 'false'.
                if (!hasRole(principal, role)) return false
            }

            // All roles have checked out ok, so the principal has
            // all the given roles.
            return true
        }
    }

    /* (non-Javadoc)
     * @see org.jsecurity.authz.AuthorizationOperations#hasRole(Object, java.lang.String)
     */
    boolean hasRole(Object principal, String role) {
        // Try the 'hasRole' method on the realm.
        try {
            return this.realm.hasRole(principal, role)
        }
        catch (MissingMethodException ex) {
            // Can't check roles, so simply return 'false'.
            return false
        }
    }

    /* (non-Javadoc)
     * @see org.jsecurity.authz.AuthorizationOperations#hasRoles(Object, java.util.List)
     */
    boolean[] hasRoles(Object principal, List<String> roles) {
        // First try the 'hasRoles' method on the realm.
        try {
            return this.realm.hasRoles(principal, roles)
        }
        catch (MissingMethodException ex) {
            // No specific method, so check each role individually.
            boolean[] retval = new boolean[roles.size()]
            try {
                for (int i in 0..<roles.size()) {
                    retval[i] = hasRole(principal, roles[i])
                }

                return retval
            }
            catch (MissingMethodException ex2) {
                // Can't check roles, so return 'false' for each role
                // requested.
                for (int i in 0..<roles.size()) {
                    retval[i] = false;
                }
                return retval
            }
        }
    }

    /* (non-Javadoc)
     * @see org.jsecurity.authz.AuthorizationOperations#isPermitted(Object, org.jsecurity.authz.Permission)
     */
    boolean isPermitted(Object principal, Permission permission) {
        // Try the 'isPermitted' method on the realm.
        try {
            return this.realm.isPermitted(principal, permission)
        }
        catch (MissingMethodException ex) {
            // Can't check permissions, so simply return 'false'.
            return false
        }
    }

    /* (non-Javadoc)
     * @see org.jsecurity.authz.AuthorizationOperations#isPermitted(Object, java.util.List)
     */
    boolean[] isPermitted(Object principal, List<Permission> permissions) {
        boolean[] retval = new boolean[permissions.size()]

        // Try the 'isPermitted' method on the realm.
        try {
            for (int i in 0..<retval.length) {
                retval[i] = this.realm.isPermitted(principal, permissions[i])
            }
        }
        catch (MissingMethodException ex) {
            // Can't check permissions, so simply return 'false' for
            // all of them.
            for (int i in 0..<retval.length) {
                retval[i] = false
            }
        }

        return retval
    }

    /* (non-Javadoc)
     * @see org.jsecurity.authz.AuthorizationOperations#isPermittedAll(Object, java.util.Collection)
     */
    boolean isPermittedAll(Object principal, Collection<Permission> permissions) {
        // Try the 'isPermittedAll' method on the realm.
        try {
            return this.realm.isPermittedAll(principal, permissions)
        }
        catch (MissingMethodException ex) {
            // No specific method, so just check each permission
            // individually until we find one that the principal
            // does not have, or we reach the end of the collection
            // of roles.
            permissions.each { permission ->
                // It the principal does not have this permission,
                // then we can immediately return 'false'.
                if (!isPermitted(principal, permission)) return false
            }

            // All permissions have checked out ok, so the principal has
            // all the given permissions.
            return true
        }
    }
}
