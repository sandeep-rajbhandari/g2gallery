import org.jsecurity.authc.AuthenticationException
import org.jsecurity.authc.UsernamePasswordToken
import org.jsecurity.SecurityUtils

class AuthController {
    def jsecSecurityManager

    def index = { redirect(action: 'login', params: params) }

    def login = {
        return [ username: params.username ]
    }

    def signIn = {
        def authToken = new UsernamePasswordToken(params.username, params.password)
        try{
            this.jsecSecurityManager.login(authToken)

            // If a controller redirected to this page, redirect back
            // to it.
            def originalParams = session.originalRequestParams
            if (originalParams){
                log.info "Redirecting to controller '${originalParams.controller}', action '${originalParams.action}'."

                // Remove the original parameters from the session.
                session.removeAttribute('originalRequestParams')

                // Redirect to the target controller and action.
                redirect(controller: originalParams.controller, action: originalParams.action, params: originalParams)
            }
            else{
                // Redirect to the home page.
                redirect(uri: '/')
            }
        }
        catch (AuthenticationException ex){
            log.info "Authentication failure for user '${params.username}'."
            flash.message = "Invalid username and/or password"
            redirect(action: 'login', params: [ username: params.username ])
        }
    }

    def signOut = {
        def threadContext = SecurityUtils.securityContext
        if (threadContext != null){
            threadContext.invalidate()
        }

        // For now, redirect back to the home page.
        redirect(uri: '/')
    }

    def unauthorized = {
        render 'You do not have permission to access this page.'
    }
}
