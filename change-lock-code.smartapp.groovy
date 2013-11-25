/**
 *  Change Lock Codes
 *
 *  Author: bigpunk6
 */

preferences {
    section("What Lock") {
		input "lock1","capability.lock", title: "Lock"
    }
    section("User") {
        input "user1", "decimal", title: "User (From 1 to 30) "
        input "code1", "decimal", title: "Code (4 to 6 digit)"
    }
}

def installed()
{
	subscribe(app, appTouch)
}

def updated()
{
	unsubscribe()
	subscribe(app, appTouch)
}

def appTouch(evt) {
    def map = "user: $user1, code: $code1"
    log.debug "$map"
	log.debug "appTouch: $evt"
	lock1.usercodechange(user1, code1)
}
