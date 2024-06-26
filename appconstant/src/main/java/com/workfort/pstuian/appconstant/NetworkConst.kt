package com.workfort.pstuian.appconstant

object NetworkConst {

    object Params {
        const val ID = "id"
        const val OLD_ID = "old_id"
        const val USER_ID = "user_id"
        const val REQUEST_ID = "request_id"
        const val LOCATION_ID = "location_id"

        const val DEVICE_ID = "device_id"
        const val FCM_TOKEN = "fcm_token"
        const val MODEL = "model"
        const val ANDROID_VERSION = "android_version"
        const val APP_VERSION_CODE = "app_version_code"
        const val APP_VERSION_NAME = "app_version_name"
        const val IP_ADDRESS = "ip_address"
        const val LAT = "lat"
        const val LNG = "lng"
        const val LOCALE = "locale"

        const val EMAIL = "email"
        const val OLD_EMAIL = "old_email"
        const val PASSWORD = "password"
        const val OLD_PASSWORD = "old_password"
        const val NEW_PASSWORD = "new_password"
        const val USER_TYPE = "user_type"

        const val NAME = "name"
        const val REG = "reg"
        const val FACULTY_ID = "faculty_id"
        const val BATCH_ID = "batch_id"
        const val SESSION = "session"

        const val DESIGNATION = "designation"
        const val DEPARTMENT = "department"

        const val IMAGE_URL = "image_url"
        const val BIO = "bio"
        const val BLOOD = "blood"
        const val BLOOD_GROUP = "blood_group"
        const val ADDRESS = "address"
        const val PHONE = "phone"
        const val CV_LINK = "cv_link"
        const val LINKED_IN = "linked_in"
        const val FB_LINK = "fb_link"

        const val DATE = "date"
        const val BEFORE_DATE = "before_date"
        const val CONTACT = "contact"
        const val INFO = "info"
        const val PRIVACY = "privacy"

        const val CREATED_AT = "created_at"
        const val UPDATED_AT = "updated_at"

        const val PAGE = "page"
        const val LIMIT = "limit"

        const val QUERY = "query"
        const val DETAILS = "details"
        const val LINK = "link"

        object UserType {
            const val STUDENT = "student"
            const val TEACHER = "teacher"
        }

        object CheckInLocation {
            const val MAIN_CAMPUS = 1
            const val SECOND_CAMPUS = 2
        }

        object Default {
            const val PAGE_SIZE = 20
        }
    }

    object Remote {
        const val PSTU_WEBSITE = "https://www.pstu.ac.bd/"

        private const val LOCAL_SERVER = "http://192.168.0.18:8888/PSTUian-web/"
        private const val DEV_SERVER = "https://dev.pstuian.com/"
        private const val LIVE_SERVER = "https://pstuian.com/"
        private const val LOCAL_API_SERVER = "${LOCAL_SERVER}api/mobile/v1/"
        private const val DEV_API_SERVER = "https://api-dev.pstuian.com/mobile/v1/"
        private const val LIVE_API_SERVER = "https://api.pstuian.com/mobile/v1/"

        private val BASE_URL = if(BuildConfig.DEBUG) DEV_SERVER else LIVE_SERVER
        val BASE_API_URL = if(BuildConfig.DEBUG) DEV_API_SERVER else LIVE_API_SERVER
        val TERMS_AND_CONDITIONS = "${BASE_URL}terms_and_conditions.php"
        val PRIVACY_POLICY = "${BASE_URL}privacy_policy.php"
        val ADMISSION_SUPPORT = "${BASE_URL}admission_support.php"

        private const val CONFIG_API_PATH = "config.php?call="
        private const val DEVICE_API_PATH = "device.php?call="
        private const val AUTH_API_PATH = "auth.php?call="
        private const val FACULTY_API_PATH = "faculty.php?call="
        private const val BATCH_API_PATH = "batch.php?call="
        private const val STUDENT_API_PATH = "student.php?call="
        private const val TEACHER_API_PATH = "teacher.php?call="
        private const val COURSE_API_PATH = "course.php?call="
        private const val EMPLOYEE_API_PATH = "employee.php?call="
        private const val NOTIFICATION_API_PATH = "notification.php?call="
        private const val BLOOD_DONATION_API_PATH = "blood_donation.php?call="
        private const val BLOOD_DONATION_REQUEST_API_PATH = "blood_donation_request.php?call="
        private const val CHECK_IN_API_PATH = "check_in.php?call="
        private const val CHECK_IN_LOCATION_API_PATH = "check_in_location.php?call="

        object Api {
            const val GET_CONFIG =  "${CONFIG_API_PATH}getLatest"

            object Device {
                const val GET_ALL = "${DEVICE_API_PATH}getAll"
                const val REGISTER = "${DEVICE_API_PATH}register"
                const val UPDATE_FCM_TOKEN = "${DEVICE_API_PATH}updateFcmToken"
            }

            object Auth {
                const val SIGN_IN = "${AUTH_API_PATH}signIn"
                const val SIGN_UP_STUDENT = "${AUTH_API_PATH}signUpStudent"
                const val SIGN_UP_TEACHER = "${AUTH_API_PATH}signUpTeacher"
                const val SIGN_OUT = "${AUTH_API_PATH}signOut"
                const val SIGN_OUT_FROM_ALL_DEVICE = "${AUTH_API_PATH}signOutFromAllDevice"
                const val CHANGE_PASSWORD = "${AUTH_API_PATH}changePassword"
                const val FORGOT_PASSWORD = "${AUTH_API_PATH}forgotPassword"
                const val EMAIL_VERIFICATION = "${AUTH_API_PATH}resendVerificationEmail"
                const val DELETE_ACCOUNT = "${AUTH_API_PATH}deleteAccount"
            }

            object Faculty {
                const val GET_ALL = "${FACULTY_API_PATH}getAll"
                const val GET = "${FACULTY_API_PATH}get"
            }

            object BATCH {
                const val GET_ALL = "${BATCH_API_PATH}getAll"
                const val GET = "${BATCH_API_PATH}get"
            }

            object Student {
                const val GET_ALL = "${STUDENT_API_PATH}getAll"
                const val GET = "${STUDENT_API_PATH}get"
                const val CHANGE_PROFILE_IMAGE = "${STUDENT_API_PATH}updateImageUrl"
                const val UPDATE_NAME = "${STUDENT_API_PATH}updateName"
                const val UPDATE_BIO= "${STUDENT_API_PATH}updateBio"
                const val UPDATE_ACADEMIC_INFO= "${STUDENT_API_PATH}updateAcademicInfo"
                const val UPDATE_CONNECT_INFO= "${STUDENT_API_PATH}updateConnectInfo"
            }

            object Teacher {
                const val GET_ALL = "${TEACHER_API_PATH}getAll"
                const val GET = "${TEACHER_API_PATH}get"
                const val CHANGE_PROFILE_IMAGE = "${TEACHER_API_PATH}updateImageUrl"
                const val UPDATE_NAME = "${TEACHER_API_PATH}updateName"
                const val UPDATE_BIO= "${TEACHER_API_PATH}updateBio"
                const val UPDATE_ACADEMIC_INFO= "${TEACHER_API_PATH}updateAcademicInfo"
                const val UPDATE_CONNECT_INFO= "${TEACHER_API_PATH}updateConnectInfo"
            }

            object Course {
                const val GET_ALL = "${COURSE_API_PATH}getAll"
            }

            object Employee {
                const val GET_ALL = "${EMPLOYEE_API_PATH}getAll"
            }

            object Notification {
                const val GET_ALL = "${NOTIFICATION_API_PATH}getAll"
            }

            object BloodDonation {
                const val GET_ALL = "${BLOOD_DONATION_API_PATH}getAll"
                const val GET = "${BLOOD_DONATION_API_PATH}get"
                const val INSERT = "${BLOOD_DONATION_API_PATH}insert"
                const val UPDATE = "${BLOOD_DONATION_API_PATH}update"
                const val DELETE = "${BLOOD_DONATION_API_PATH}delete"
            }

            object BloodDonationRequest {
                const val GET_ALL = "${BLOOD_DONATION_REQUEST_API_PATH}getAll"
                const val GET = "${BLOOD_DONATION_REQUEST_API_PATH}get"
                const val INSERT = "${BLOOD_DONATION_REQUEST_API_PATH}insert"
                const val UPDATE = "${BLOOD_DONATION_REQUEST_API_PATH}update"
                const val DELETE = "${BLOOD_DONATION_REQUEST_API_PATH}delete"
            }

            object CheckIn {
                const val GET_ALL = "${CHECK_IN_API_PATH}getAll"
                const val GET = "${CHECK_IN_API_PATH}get"
                const val CHECK_IN = "${CHECK_IN_API_PATH}checkIn"
                const val PRIVACY = "${CHECK_IN_API_PATH}privacy"
                const val DELETE = "${CHECK_IN_API_PATH}delete"
            }

            object CheckInLocation {
                const val GET_ALL = "${CHECK_IN_LOCATION_API_PATH}getAll"
                const val GET = "${CHECK_IN_LOCATION_API_PATH}get"
                const val SEARCH = "${CHECK_IN_LOCATION_API_PATH}search"
                const val INSERT = "${CHECK_IN_LOCATION_API_PATH}insert"
                const val UPDATE = "${CHECK_IN_LOCATION_API_PATH}update"
            }
        }
    }
}