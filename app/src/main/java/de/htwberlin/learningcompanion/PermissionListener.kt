package de.htwberlin.learningcompanion

interface PermissionListener {
    fun onPermissionAccepted(permission: String)
    fun onPermissionRevoked(permission: String)

}
