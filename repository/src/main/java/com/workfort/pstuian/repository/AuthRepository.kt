package com.workfort.pstuian.repository

import com.workfort.pstuian.model.ConfigEntity
import com.workfort.pstuian.model.DeviceEntity
import com.workfort.pstuian.model.StudentEntity
import com.workfort.pstuian.model.TeacherEntity

interface AuthRepository {
 suspend fun getConfig(): ConfigEntity
 suspend fun getAllDevices(page: Int): List<DeviceEntity>
 suspend fun registerDevice(
  fcmToken: String,
  lat: String = "0.0",
  lng: String = "0.0",
 ): DeviceEntity
 suspend fun updateFcmToken(
  fcmToken: String
 ): DeviceEntity
 fun getSignInUserType(): String
 fun getSignInUser(): Any
 fun getUserIdAndType() : Pair<Int, String>
 suspend fun storeSignInStudent(student: StudentEntity)
 suspend fun storeSignInTeacher(teacher: TeacherEntity)
 suspend fun signIn(email: String, password: String, userType: String): Any
 suspend fun signUpStudent(
  name: String,
  id: String,
  reg: String,
  facultyId: Int,
  batchId: Int,
  session: String,
  email: String
 ): StudentEntity
 suspend fun signUpTeacher(
  name: String,
  designation: String,
  department: String,
  email: String,
  password: String,
  facultyId: Int,
 ): TeacherEntity
 suspend fun signOut(fromAllDevice: Boolean = false): String
 suspend fun changePassword(oldPassword: String, newPassword: String): String
 suspend fun forgotPassword(userType: String, email: String): String
 suspend fun emailVerification(userType: String, email: String): String
 suspend fun deleteAll()
 suspend fun updateDataRefreshState()
 suspend fun deleteAccount(password: String): String
}