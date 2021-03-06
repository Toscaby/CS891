package edu.vandy.app

import android.app.Application
import com.squareup.picasso.Picasso
import edu.vandy.app.extensions.DelegatesExt.notNullSingleValue
import edu.vandy.app.extensions.ImageDownloader
import edu.vandy.app.preferences.CompositeUnsubscriber

/**
 * The application made to be easily accessible as a singleton.
 */
class App : Application() {
    companion object {
        @JvmStatic
        var instance: App by notNullSingleValue()
    }

    // Set by ImageDownloader object but it can't be defined
    // in that object because static classes aren't supposed
    // to hold references to contexts (also breaks instant run).
    lateinit var picasso: Picasso

    /**
     * For extension or Object ObservablePreference declarations
     * (probably not necessary to unsubscribe from object singletons
     * or for extension properties, but it never hurts to be careful).
     */
    val compositeUnsubscriber = CompositeUnsubscriber()

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Let the ImageDownloader extension class do the
        // initialization of Picasso.
        ImageDownloader.installPicasso(this)
    }

    override fun onTerminate() {
        compositeUnsubscriber.invoke()
        super.onTerminate()
    }
}
