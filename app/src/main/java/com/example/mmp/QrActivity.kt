package com.example.mmp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

class QrActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        integrator.setPrompt("Сканируйте QR код на вашем столе")
        integrator.setCameraId(0)
        integrator.setBeepEnabled(false)
        integrator.setBarcodeImageEnabled(false)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Log.e("Scan*******", "Cancelled scan")
            } else {
                Log.e("Scan", "Scanned")
                val int = Intent()
                int.putExtra("numberTable", result.contents.toInt())
                setResult(Activity.RESULT_OK, int)
            }
            finish()
        } else { // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}
