package com.workfort.pstuian.app.data.local.database;

public interface ColumnNames {
    String SL = "sl";

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
    }

    interface Teacher {
        String ID = "id";
        String NAME = "name";
        String DESIGNATION = "designation";
        String STATUS = "status";
        String PHONE = "phone";
        String LINKED_IN = "linked_in";
        String FB_LINK = "fb_link";
        String ADDRESS = "address";
        String EMAIL = "email";
        String DEPARTMENT = "department";
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