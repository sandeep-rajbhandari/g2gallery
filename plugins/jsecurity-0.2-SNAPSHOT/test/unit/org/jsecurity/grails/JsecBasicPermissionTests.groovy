package org.jsecurity.grails

import org.jsecurity.authz.Permission

/**
 * Test case for {@link JsecBasicPermission}.
 */
class JsecBasicPermissionTests extends GroovyTestCase {
    /**
     * Tests that {@link JsecBasicPermission#getActions()} returns
     * the expected values with permission instances created via
     * different constructor arguments.
     */
    void testActionsSet() {
        def testPermission = new JsecBasicPermission('book', [ 'view', 'modify', 'create', 'delete' ])
        assert testPermission.actions == [ 'view', 'modify', 'create', 'delete' ] as Set
        assert testPermission.target == 'book'

        testPermission = new JsecBasicPermission('person', 'view, modify, create, delete')
        assert testPermission.actions == [ 'view', 'modify', 'create', 'delete' ] as Set
        assert testPermission.target == 'person'

        testPermission = new JsecBasicPermission('person', 'view;create;    modify')
        assert testPermission.actions == [ 'view', 'modify', 'create' ] as Set

        testPermission = new JsecBasicPermission('person', 'sit')
        assert testPermission.actions == [ 'sit' ] as Set

        testPermission = new JsecBasicPermission('person', [ 'sit' ])
        assert testPermission.actions == [ 'sit' ] as Set
    }

    /**
     * Tests that {@link JsecBasicPermission#getActionsString()} returns
     * the expected standard string with permission instances created
     * via different constructor arguments.
     */
    void testActionsString() {
        def testPermission = new JsecBasicPermission('book', [ 'view', 'modify', 'create', 'delete' ])
        def splitString = Arrays.asList(testPermission.actionsString.split(/,/)) as Set
        assert splitString == [ 'view', 'modify', 'create', 'delete' ] as Set
        assert testPermission.target == 'book'

        testPermission = new JsecBasicPermission('person', 'view, modify, create, delete')
        splitString = Arrays.asList(testPermission.actionsString.split(/,/)) as Set
        assert splitString == [ 'view', 'modify', 'create', 'delete' ] as Set
        assert testPermission.target == 'person'

        testPermission = new JsecBasicPermission('person', 'view;create;    modify')
        splitString = Arrays.asList(testPermission.actionsString.split(/,/)) as Set
        assert splitString == [ 'view', 'modify', 'create' ] as Set

        testPermission = new JsecBasicPermission('person', 'sit')
        assert testPermission.actionsString == 'sit'

        testPermission = new JsecBasicPermission('person', [ 'sit' ])
        assert testPermission.actionsString == 'sit'
    }

    /**
     * Tests that {@link JsecBasicPermission#implies(Permission p)
     * returns the correct value when used to compare a variety of
     * different permissions.
     */
    void testImplies() {
        def requiredPermission = new JsecBasicPermission('book', [ 'delete' ])

        def testPermission = new JsecBasicPermission('book', 'delete')
        assert testPermission.implies(requiredPermission)

        testPermission = new JsecBasicPermission('shell', 'delete')
        assert !testPermission.implies(requiredPermission)

        testPermission = new JsecBasicPermission('*', 'delete')
        assert testPermission.implies(requiredPermission)

        testPermission = new JsecBasicPermission('book', 'view, modify, create, delete')
        assert testPermission.implies(requiredPermission)

        testPermission = new JsecBasicPermission('book', '*')
        assert testPermission.implies(requiredPermission)

        testPermission = new JsecBasicPermission('shell', '*')
        assert !testPermission.implies(requiredPermission)

        testPermission = new JsecBasicPermission('*', '*')
        assert testPermission.implies(requiredPermission)

        requiredPermission = [ name: 'book', actions: [ 'delete' ] as Set ] as Permission

        testPermission = new JsecBasicPermission('book', 'delete')
        assert !testPermission.implies(requiredPermission)

        requiredPermission = new JsecBasicPermission('book', [ 'view', 'create', 'edit' ])

        testPermission = new JsecBasicPermission('book', '*')
        assert testPermission.implies(requiredPermission)

        testPermission = new JsecBasicPermission('book', 'view')
        assert !testPermission.implies(requiredPermission)

        testPermission = new JsecBasicPermission('book', 'view, create,edit,delete')
        assert testPermission.implies(requiredPermission)
    }
}
