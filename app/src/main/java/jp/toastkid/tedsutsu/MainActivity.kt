package jp.toastkid.tedsutsu

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import jp.toastkid.tedsutsu.about.AboutThisAppFragment
import jp.toastkid.tedsutsu.app_list.AppListFragment
import jp.toastkid.tedsutsu.preference.AppNamePreference
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (attemptToLaunch(intent)) {
            return
        }

        val fragment = AppListFragment()
        switchFragment(fragment)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        attemptToLaunch(intent)
    }

    fun switchAboutFragment() {
        switchFragment(AboutThisAppFragment())
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager?.beginTransaction()?.also {
            it.setCustomAnimations(R.anim.slide_in_right, 0, 0, android.R.anim.slide_out_right)
            it.replace(R.id.fragment_container, fragment)
            it.addToBackStack("${fragment.hashCode()}")
            it.commitAllowingStateLoss()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager.fragments.size == 0) {
            finish()
        }
    }

    private fun attemptToLaunch(intent: Intent?): Boolean {
        if (intent == null || !TextUtils.equals(Intent.ACTION_ASSIST, intent.action)) {
            return false
        }
        val packageName = AppNamePreference(this).readAppId()
        if (TextUtils.isEmpty(packageName)) {
            Snackbar.make(fragment_container, "You should register any app.", Snackbar.LENGTH_SHORT)
            return false
        }
        val launchApp = packageManager.getLaunchIntentForPackage(packageName) ?: return false
        try {
            startActivity(launchApp)
            finish()
            return true
        } catch (e: ActivityNotFoundException) {
            Snackbar.make(fragment_container, "It has cannot launch app.", Snackbar.LENGTH_SHORT)
            Timber.e(e)
        }
        return false
    }

}
