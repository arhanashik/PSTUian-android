package com.workfort.pstuian.util.helper

import android.text.InputFilter

/**
 *  ****************************************************************************
 *  * Created by : arhan on 10 Dec, 2021 at 0:56.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

val nameFilter = InputFilter { source, start, end, _, _, _ ->
    for(i in start until end) {
        if (!(Character.isLetter(source[i]) ||
                    source[i].toString() == "_" ||
                    source[i].toString() == "-" ||
                    source[i].toString() == "." ||
                    source[i].toString() == " "
                    )) {
            return@InputFilter ""
        }
    }
    null
}