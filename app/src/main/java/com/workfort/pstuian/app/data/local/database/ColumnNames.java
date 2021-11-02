package com.workfort.pstuian.app.data.local.database;

public interface ColumnNames {
    String SL = "sl";

    interface Config {
        String ID = "id";
        String ANDROID_VERSION = "android_version";
        String IOS_VERSION = "ios_version";
        String DATA_REFRESH_VERSION = "data_refresh_version";
        String API_VERSION = "api_version";
        String ADMIN_API_VERSION = "admin_api_version";
        String FORCE_REFRESH = "force_refresh";
        String FORCE_UPDATE = "force_update";
        String FORCE_REFRESH_DONE = "force_refresh_done";
        String FORCE_UPDATE_DONE = "force_update_done";
    }

    interface Slider {
        String ID = "id";
        String TITLE = "title";
        String IMAGE_URL = "image_url";
    }

    interface Faculty {
        String ID = "id";
        String SHORT_TITLE = "short_title";
        String TITLE = "title";
    }

    interface Student {
        String NAME = "name";
        String ID = "id";
        String REG = "reg";
        String PHONE = "phone";
        String LINKED_IN = "linked_in";
        String FB_LINK = "fb_link";
        String BLOOD = "blood";
        String ADDRESS = "address";
        String EMAIL = "email";
        String BATCH_ID = "batch_id";
        String SESSION = "session";
        String FACULTY_ID = "faculty_id";
        String IMAGE_URL = "image_url";
        String CV_LINK = "cv_link";
        String BIO = "bio";
    }

    interface Teacher {
        String ID = "id";
        String NAME = "name";
        String DESIGNATION = "designation";
        String BIO = "bio";
        String PHONE = "phone";
        String LINKED_IN = "linked_in";
        String FB_LINK = "fb_link";
        String ADDRESS = "address";
        String EMAIL = "email";
        String DEPARTMENT = "department";
        String BLOOD = "blood";
        String FACULTY_ID = "faculty_id";
        String IMAGE_URL = "image_url";
    }

    interface Employee {
        String ID = "id";
        String NAME = "name";
        String DESIGNATION = "designation";
        String DEPARTMENT = "department";
        String PHONE = "phone";
        String ADDRESS = "address";
        String FACULTY_ID = "faculty_id";
        String IMAGE_URL = "image_url";
    }

    interface Batch {
        String ID = "id";
        String NAME = "name";
        String TITLE = "title";
        String SESSION = "session";
        String FACULTY_ID = "faculty_id";
        String TOTAL_STUDENT = "total_student";
    }

    interface Course {
        String ID = "id";
        String COURSE_CODE = "course_code";
        String COURSE_TITLE = "course_title";
        String CREDIT_HOUR = "credit_hour";
        String FACULTY_ID = "faculty_id";
        String STATUS = "status";
    }
}