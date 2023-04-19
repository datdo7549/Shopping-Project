package com.example.shoppingproject.feature.scan

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.SurfaceHolder
import androidx.core.app.ActivityCompat
import androidx.fragment.app.setFragmentResult
import com.example.shoppingproject.databinding.FragmentScannedBinding
import com.example.shoppingproject.feature.base.BaseFragment
import com.example.shoppingproject.feature.homepage.HomeViewModel
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Detector.Detections
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException

class ScannedFragment : BaseFragment<FragmentScannedBinding, HomeViewModel>() {
    override val viewModel: HomeViewModel by viewModel()

    private var barcodeDetector: BarcodeDetector? = null
    private var cameraSource: CameraSource? = null

    override fun onCreateViewBinding(inflater: LayoutInflater): FragmentScannedBinding {
        return FragmentScannedBinding.inflate(inflater)
    }

    override fun onResume() {
        super.onResume()
        initialiseDetectorsAndSources()
    }

    override fun onPause() {
        super.onPause()
        cameraSource?.release()
    }

    private val handler = Handler(Looper.getMainLooper())


    private fun initialiseDetectorsAndSources() {
        barcodeDetector = BarcodeDetector.Builder(requireContext())
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()
        barcodeDetector?.let {
            cameraSource = CameraSource.Builder(requireContext(), it)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build()
        }
        viewBinding.surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(
                            requireActivity(),
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        cameraSource?.start(viewBinding.surfaceView.holder)
                    } else {
                        requestPermissions(
                            arrayOf(Manifest.permission.CAMERA),
                            REQUEST_CAMERA_PERMISSION
                        )
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource?.stop()
            }
        })
        barcodeDetector?.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}
            override fun receiveDetections(detections: Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() != 0) {
                    if (barcodes.valueAt(0).displayValue.length > 5) {
                        handler.post {
                            val bundle = Bundle().apply {
                                putString("product_id_key", barcodes.valueAt(0).displayValue)
                            }
                            setFragmentResult("result_key", bundle)
                            navigateUp()
                        }
                    }
                }
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                cameraSource?.start(viewBinding.surfaceView.holder)
                return
            }
        }
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 201
        fun newInstance() = ScannedFragment()
    }
}