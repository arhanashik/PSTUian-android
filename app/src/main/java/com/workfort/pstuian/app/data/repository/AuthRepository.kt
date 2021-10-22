package com.workfort.pstuian.app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.workfort.pstuian.PstuianApp
import com.workfort.pstuian.app.data.local.config.ConfigEntity
import com.workfort.pstuian.app.data.local.config.ConfigService
import com.workfort.pstuian.app.data.local.pref.Prefs
import com.workfort.pstuian.app.data.local.student.StudentEntity
import com.workfort.pstuian.app.data.remote.apihelper.AuthApiHelper
import com.workfort.pstuian.util.helper.GsonUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import timber.log.Timber

/**
 *  ****************************************************************************
 *  * Created by : arhan on 14 Oct, 2021 at 4:03 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 10/14/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class AuthRepository(
    private val dbService: ConfigService,
    private val helper: AuthApiHelper
) {

    private val Context.authStore: DataStore<Preferences> by preferencesDataStore(AUTH_PREFERENCES)
    companion object {
        private val USER = stringPreferencesKey("user")
        private val AUTH_TOKEN = stringPreferencesKey("auth_token")
        private const val AUTH_PREFERENCES = "auth_preferences"
    }

    suspend fun getConfig(): ConfigEntity {
        val oldConfig = dbService.getLatest()
        return try {
            var newConfig = helper.getConfig()
            if(oldConfig == null || oldConfig != newConfig) {
                dbService.insert(newConfig)
            } else {
                // pass local states(forceRefreshDone and forceUpdateDone) into the new state
                if(oldConfig == newConfig) newConfig = oldConfig
            }

            newConfig
        } catch (ex: Exception) {
            Timber.e(ex)
            if(oldConfig == null) throw ex
            oldConfig
        }
    }

    fun getSignedInUserFlow(): Flow<StudentEntity> {
        val context = PstuianApp.getBaseApplicationContext()
        return context.authStore.data
            .map { auth ->
                val jsonStr = auth[USER]?: ""
                GsonUtil.fromJson(jsonStr)
            }
    }

    fun getSignInUser(): StudentEntity {
        val context = PstuianApp.getBaseApplicationContext()
        val jsonStr = runBlocking {
            context.authStore.data.first()
        }[USER]?: throw Exception("No User Found!")

        return GsonUtil.fromJson(jsonStr)
    }

    suspend fun storeSignInUser(student: StudentEntity) {
        val context = PstuianApp.getBaseApplicationContext()
        context.authStore.edit { auth ->
            auth[USER] = GsonUtil.toJson(student)
        }
    }

    private suspend fun storeAuthToken(token: String) {
        val context = PstuianApp.getBaseApplicationContext()
        context.authStore.edit { auth ->
            auth[AUTH_TOKEN] = token
        }
        Prefs.authToken = token
    }

    suspend fun signIn(email: String, password: String, userType: String): StudentEntity {
        val data = helper.signIn(email, password, userType)
        storeSignInUser(data.first)
        storeAuthToken(data.second)

        return data.first
    }

    suspend fun signUp(
        name: String,
        id: String,
        reg: String,
        facultyId: Int,
        batchId: Int,
        session: String
    ): StudentEntity {
        val data = helper.signUp(name, id, reg, facultyId, batchId, session)
        storeSignInUser(data.first)
        storeAuthToken(data.second)

        return data.first
    }

    suspend fun signOut(id: Int, userType: String): String {
        val data = helper.signOut(id, userType)
        deleteAll()

        return data
    }

    suspend fun deleteAll() {
        val context = PstuianApp.getBaseApplicationContext()
        context.authStore.edit { auth ->
            auth.remove(USER)
            auth.remove(AUTH_TOKEN)
        }
        Prefs.authToken = ""
    }

    suspend fun updateDataRefreshState() {
        val config = dbService.getLatest()
        config?.let {
            it.forceRefreshDone = true
            dbService.update(it)
        }
    }
}