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
        input "user1", "enum", title: "User", metadata: [values: ["1","2","3","4","5","6","7","8","9","10","11","12","13"]]
		input "code1","number"
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
