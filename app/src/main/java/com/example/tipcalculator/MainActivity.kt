package com.example.tipcalculator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

private const val INITIAL_TIP_PERCENT = 15
private const val DEFAULT_TOTAL_AMOUNT = 0.00
private const val DEFAULT_TIP_AMOUNT = 0.00
private const val DEFAULT_SPLIT_VALUE = 2

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercent.text = "$INITIAL_TIP_PERCENT%"
        tvTotalAmount.text = DEFAULT_TOTAL_AMOUNT.toString()
        tvTipAmount.text = DEFAULT_TIP_AMOUNT.toString()
        seekBarSplit.progress = DEFAULT_SPLIT_VALUE

        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{

            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                tvTipPercent.text = "$progress%"
                updateTipDescription(progress)
                computeTipAndTotal()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        etBase.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                computeTipAndTotal()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })


        seekBarSplit.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{

            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                tvSplitResult.text = "$progress"

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription: String
        when(tipPercent){
          in 1..9 -> tipDescription = "Poor"
            in 9..14 -> tipDescription = "Acceptable"
            in 14..19 ->tipDescription = "Good"
            in 19..24 ->tipDescription = "Great"
            else -> tipDescription = "Amazing"

        }

    }

    private fun computeTipAndTotal() {
        if (etBase.text.isEmpty()){
            tvTipAmount.text = DEFAULT_TIP_AMOUNT.toString()
            tvTotalAmount.text = DEFAULT_TOTAL_AMOUNT.toString()
            return
        }
        val baseAmount = etBase.text.toString().toDouble()
        val tipPercent = seekBarTip.progress
        val tipAmount  = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipPercent


        tvTipAmount.text = "%.2f".format(tipAmount)
        tvTotalAmount.text = "%.2f".format(totalAmount)

    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
    }
}