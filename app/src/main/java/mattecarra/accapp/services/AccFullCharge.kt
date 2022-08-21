package mattecarra.accapp.services

import android.Manifest
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import mattecarra.accapp.R
import mattecarra.accapp.acc.Acc
import kotlin.coroutines.CoroutineContext

@TargetApi(Build.VERSION_CODES.N)
class AccFullCharge: TileService(), CoroutineScope {
    private val LOG_TAG = "AccFullCharge"

    protected lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate() {
        super.onCreate()
        job = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onClick() {
        super.onClick()

        launch {
            if(qsTile.state==Tile.STATE_ACTIVE) {
                qsTile.state = Tile.STATE_INACTIVE
                Acc.instance.accRestartDaemon()
                Log.d(LOG_TAG, "Disable Full Charge")
            }
            else
            {
                qsTile.state = Tile.STATE_ACTIVE
                Acc.instance.setChargingLimitForOneCharge(100)
                Log.d(LOG_TAG, "Charging to max")
            }
            qsTile.updateTile()
        }
    }

    override fun onTileAdded() {
        super.onTileAdded()
        // Do something when the user add the Tile
    }
}