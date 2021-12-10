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
 *  *
 *  * Last edited by : arhan on 2021/12/10.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
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