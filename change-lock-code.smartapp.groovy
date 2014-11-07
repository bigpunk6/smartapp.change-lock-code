/**
 *  Change Lock Codes
 *
 *  Author: bigpunk6
 */


// Automatically generated. Make future change here.
definition(
    name: "Change Lock Codes",
    namespace: "",
    author: "mkurtzjr@live.com",
    description: "Change Lock Codes",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience%402x.png")

preferences {
    section("What Lock") {
                input "lock1","capability.lock", title: "Lock"
    }
    section("User") {
        input "user1", "decimal", title: "User (From 1 to 30) "
        input "code1", "decimal", title: "Code (4 to 8 digits)"
        input "delete1", "enum", title: "Delete User", required: false, metadata: [values: ["Yes","No"]]
    }
}

def installed()
{
        subscribe(app, appTouch)
        subscribe(lock1, "code$user1", usercodeget)
}

def updated()
{
        unsubscribe()
        subscribe(app, appTouch)
        subscribe(lock1, "code$user1", usercodeget)
}

def appTouch(evt) {
    log.debug "Current Code for user $user1: $lock1.currentcode$user1"
    log.debug "user: $user1, code: $code1"
    log.debug "$delete1 to Delete User"
    if (delete1 == "Yes") {
        lock1.deleteCode(user1)
    } else {
        lock1.setCode(user1, code1)
    }
}

def usercodeget(evt){
    log.debug "Current Code for user $user1: $lock1.currentcode$user1"
}
