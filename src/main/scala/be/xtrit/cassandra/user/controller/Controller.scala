package be.xtrit.cassandra.user.controller

import be.xtrit.cassandra.user.EmailAddressIsAlreadyInUseException
import be.xtrit.cassandra.user.service.{UserRegistrationInformation, LoginRequest, UserService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation._

@RestController
class Controller {

    @Autowired
    private var userService: UserService = null

    @RequestMapping(value = Array("/login"), method = Array(RequestMethod.POST))
    def login(@RequestBody loginRequest: LoginRequest): ResponseEntity[Void] = {
        val isValid = userService.validateCredentials(loginRequest)

        if(isValid)
            ResponseEntity.ok.build
        else
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build
    }

    @RequestMapping(value = Array("/register"), method = Array(RequestMethod.POST))
    @ResponseStatus(value = HttpStatus.OK)
    def register(@RequestBody userRegistrationInformation: UserRegistrationInformation): ResponseEntity[_] = {
        try {
            userService.registerUser(userRegistrationInformation)
        } catch {
            case e: EmailAddressIsAlreadyInUseException =>
                return ResponseEntity.badRequest.body("Email address is already in use")
        }

        ResponseEntity.noContent().build
    }
}

