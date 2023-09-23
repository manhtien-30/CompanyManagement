package com.example.companymanagement

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.companymanagement.viewcontroller.fragment.mainhome.MainHome
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    //    @get:Rule
//    var activityTestRule = ActivityScenarioRule(MainActivity::class.java)


     @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.companymanagement", appContext.packageName)
    }

    @Test
    fun testFragmentA() {
        val fragmentArgs =
            bundleOf("selectedListItem" to 0) // nhung thu can thiet cho fragment // mac dinh khong can pass
        val scenario = launchFragmentInContainer<MainHome>(fragmentArgs)
        //test click button
//        onView(withId(R.id.b_send_mail)).perform(click())
        //test cycle
//        scenario.moveToState(Lifecycle.State.CREATED)
        //test method
//        scenario.onFragment { fragment ->
//            fragment.methodgido()
//        }
    }
}