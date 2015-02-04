/**
 *  Change or Delete Lock Codes + Notify
 *
 *  Author: bigpunk6
 */
 
definition(
    name: "Change or Delete Door Lock Codes - Notify",
    namespace: "bigpunk6",
    author: "bigpunk6",
    description: "This app alows you to change or delete the user codes for your smart door lock",
    category: "Safety & Security",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience%402x.png")

import groovy.json.JsonSlurper

preferences {
    section("What Lock") {
                input "lock","capability.lock", title: "Lock"
    }
    section("User") {
        input "username", "text", title: "Name for User"
        input "user", "number", title: "User Slot (From 1 to 30) "
        input "code", "text", title: "Code (4 to 8 digits) or X to Delete"
    }
    section( "Notifications" ) {
        input "sendCode", "enum", title: "Send notification when users code is used", metadata:[values:["Yes","No"]], required:false
        input "sendPushMessage", "enum", title: "Send a push notification?", metadata:[values:["Yes","No"]], required:false
        input "phone", "phone", title: "Send a Text Message?", required: false
    }
}

def installed()
{
        subscribe(app, appTouch)
        subscribe(lock, "codeReport", codereturn)
        subscribe(lock, "lock", codeUsed)
}

def updated()
{
        unsubscribe()
        subscribe(app, appTouch)
        subscribe(lock, "codeReport", codereturn)
        subscribe(lock, "lock", codeUsed)
}

def appTouch(evt) {
    
    if (code.equalsIgnoreCase("X")) {
        lock.deleteCode(user)
    } else {
        lock.setCode(user, code)
    }
}

def codereturn(evt) {
	def codenumber = evt.data.replaceAll("\\D+","");
    if (evt.value == user) {
        if (codenumber == "") {
            def message = "User $username in user slot $evt.value code is not set or was deleted on $lock"
            send(message)
        } else {
            def message = "Code for user $username in user slot $evt.value was set to $codenumber on $lock"
            send(message)
        }
    }
}

def codeUsed(evt) {
    if(evt.value == "unlocked" && evt.data) {
    	def codeData = new JsonSlurper().parseText(evt.data)
        def message = "$lock was unlocked by $username in user slot $codeData.usedCode"
        if(codeData.usedCode == user && sendCode == "Yes") {
            send(message)
        }
    }
}

private send(msg) {
    if (sendPushMessage == "Yes") {
        sendPush(msg)
    }

    if (phone) {
        sendSms(phone, msg)
    }
}
