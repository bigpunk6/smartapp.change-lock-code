/**
 *  Change or Delete Lock Codes
 *
 *  Author: bigpunk6
 */
 
definition(
    name: "Change or Delete Lock Codes",
    namespace: "",
    author: "bigpunk6",
    description: "Change or Delete Lock Codes",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience%402x.png")

preferences {
    section("What Lock") {
                input "lock","capability.lock", title: "Lock"
    }
    section("User") {
        input "user", "number", title: "User (From 1 to 30) "
        input "code", "text", title: "Code (4 to 8 digits) or X to Delete"
    }
}

def installed()
{
        subscribe(app, appTouch)
        subscribe(lock, "codeReport", codereturn)
}

def updated()
{
        unsubscribe()
        subscribe(app, appTouch)
        subscribe(lock, "codeReport", codereturn)
}

def appTouch(evt) {
    
    if (code.equalsIgnoreCase("X")) {
        log.debug "Deleting user $user"
        lock.deleteCode(user)
    } else {
        log.debug "Set user: $user, code: $code"
        lock.setCode(user, code)
    }
}

def codereturn(evt){
	def codenumber = evt.data.replaceAll("\\D+","");
    if (codenumber == "") {
        log.debug "User $evt.value was deleted"
    } else {
        log.debug "Current Code for user $evt.value is $codenumber"
    }
}
