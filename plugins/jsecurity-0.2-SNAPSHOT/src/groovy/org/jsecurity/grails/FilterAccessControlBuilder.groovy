package org.jsecurity.grails

import org.jsecurity.authz.Permission
import org.jsecurity.context.SecurityContext

class FilterAccessControlBuilder {
    private SecurityContext securityContext

    FilterAccessControlBuilder(SecurityContext securityContext) {
        this.securityContext = securityContext
    }

    /**
     * Checks whether the user associated with the builder's security
     * context has the given role or not.
     */
    boolean role(String roleName) {
        return this.securityContext.hasRole(roleName)
    }

    /**
     * Checks whether the user associated with the builder's security
     * context has the given permission or not.
     */
    boolean permission(Permission permission) {
        return this.securityContext.isPermitted(permission)
    }

    /**
     * <p>Checks whether the user associated with the builder's security
     * context has permission for a given type and set of actions. The
     * map must have 'type' and 'actions' entries. The method should be
     * called like this:</p>
     * <pre>
     *     permission(type: 'profile', actions: 'edit')
     *     permission(type: 'book', actions: [ 'show', 'modify' ])
     *     permission(type: 'book', actions: 'show, modify')
     * </pre>
     */
    boolean permission(Map args) {
        // First check that the argument map has the required entries.
        if (!args['type']) {
            throw new IllegalArgumentException("The 'type' parameter must be provided to 'permission()'")
        }
        else if (!(args['type'] instanceof String)) {
            throw new IllegalArgumentException("The 'type' parameter must be a string.")
        }

        if (!args['actions']) {
            throw new IllegalArgumentException("The 'actions' parameter must be provided to 'permission()'")
        }
        else if (!(args['actions'] instanceof String || args['actions'] instanceof Collection)) {
            throw new IllegalArgumentException("The 'actions' parameter must be a string or a collection of strings.")
        }

        // Create a new permission from the given parameters.
        def p = new JsecBasicPermission(args['type'], args['actions'])

        // Check whether the currently authenticated user has the
        // permission.
        return this.securityContext.isPermitted(p)
    }
}
